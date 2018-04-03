package com.example.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.FakultasModel;
import com.example.model.KelulusanModel;
import com.example.model.ProdiModel;
import com.example.model.StudentDataTableModel;
import com.example.model.StudentModel;
import com.example.model.UniversitasModel;
import com.example.service.StudentService;

@Controller
public class StudentController {
	@Autowired
	StudentService studentDAO;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/mahasiswa/tambah")
	public String add(Model model) {
		model.addAttribute("student", new StudentModel());
		return "form-add";
	}

	@RequestMapping("/mahasiswa/tambah/submit")
	public String addSubmit(Model model, @ModelAttribute("student") StudentModel student) {

		ProdiModel prodi = studentDAO.selectProdi(student.getId_prodi());
		System.out.println(student.getId_prodi() + ", " + prodi.getKode_prodi());
		FakultasModel fakultas = studentDAO.selectFakultas(prodi.getId_fakultas());
		UniversitasModel univ = studentDAO.selectUniv(fakultas.getId_univ());
		String lastNPMInserted = studentDAO.getLastNPMInserted(student.getId_prodi());
		System.out.println(student.getId_prodi());

		int kodeJalurMasuk;
		String jalurMasuk;
		switch (student.getJalur_masuk()) {
		case "UndanganOlimpiade":
			kodeJalurMasuk = 53;
			jalurMasuk = "Undangan Olimpiade";
			break;
		case "UndanganRegulerSNMPTN":
			kodeJalurMasuk = 54;
			jalurMasuk = "Undangan Reguler / SNMPTN";
			break;
		case "UndanganParalelPPKB":
			kodeJalurMasuk = 55;
			jalurMasuk = "Undangan Paralel / PPKB";
			break;
		case "UjianTulisBersamaSBMPTN":
			kodeJalurMasuk = 57;
			jalurMasuk = "Ujian Tulis Bersama / SBMPTN";
			break;
		case "UjianTulisMandiri":
			kodeJalurMasuk = 62;
			jalurMasuk = "Ujian Tulis Mandiri";
			break;
		default:
			kodeJalurMasuk = 0;
			jalurMasuk = "";
			break;
		}
		String tahunMasuk = student.getTahun_masuk().substring(2, student.getTahun_masuk().length());
		int kodeUniv = Integer.parseInt(univ.getKode_univ());
		int kodeProdi = Integer.parseInt(prodi.getKode_prodi());
		int lastInsertId = Integer.parseInt(lastNPMInserted.substring(9, lastNPMInserted.length())) + 1;
		String npm = tahunMasuk + kodeUniv + kodeProdi + kodeJalurMasuk + lastInsertId;
		student.setNpm(npm);
		student.setJalur_masuk(jalurMasuk);
		if (studentDAO.addStudent(student)) {
			model.addAttribute("student", student);
			return "success-add";
		} else {
			return "form-add";
		}

	}

	@RequestMapping("/mahasiswa")
	public String view(Model model, @RequestParam(value = "npm", required = true) String npm) {

		StudentModel student = studentDAO.selectStudent(npm);

		if (student != null) {
			ProdiModel prodi = studentDAO.selectProdi(student.getId_prodi());
			FakultasModel fakultas = studentDAO.selectFakultas(prodi.getId_fakultas());
			UniversitasModel univ = studentDAO.selectUniv(fakultas.getId_univ());
			model.addAttribute("student", student);
			model.addAttribute("prodi", prodi);
			model.addAttribute("fakultas", fakultas);
			model.addAttribute("univ", univ);

			return "view";
		} else {
			model.addAttribute("npm", npm);
			return "not-found";
		}

	}

	@RequestMapping("/student/view/{npm}")
	public String viewPath(@Valid Model model, @PathVariable(value = "npm") String npm) {
		StudentModel student = studentDAO.selectStudent(npm);

		if (student != null) {
			model.addAttribute("student", student);
			return "view";
		} else {
			model.addAttribute("npm", npm);
			return "not-found";
		}
	}

	@RequestMapping("/kelulusan")
	public String kelulusan() {
		return "kelulusan";
	}

	@RequestMapping("/kelulusan/submit")
	public String kelulusanSubmit(Model model, @RequestParam(value = "tahun_masuk", required = true) int tahun_masuk,
			@RequestParam(value = "id_prodi", required = true) int id_prodi) {
		int jumlahMahasiswa = 0;
		int jumlahMahasiswaLulus = 0;
		double persentaseKelulusan;
		List<KelulusanModel> kelulusan = studentDAO.getKelulusan(tahun_masuk, id_prodi);
		if (kelulusan.size() > 0) {
			for(int i=0; i<kelulusan.size(); i++) {
				if(kelulusan.get(i).getStatus().equals("Lulus")) {
					System.out.println(""+kelulusan.get(i).getStatus()+", "+kelulusan.get(i).getJumlah());
					jumlahMahasiswaLulus += kelulusan.get(i).getJumlah();
				}
				jumlahMahasiswa += kelulusan.get(i).getJumlah();
				System.out.println(""+kelulusan.get(i).getStatus()+", "+kelulusan.get(i).getJumlah());
			}
			persentaseKelulusan = ((double)jumlahMahasiswaLulus / (double)jumlahMahasiswa) * 100;
			
			System.out.println(""+(9/22) * 100);
			model.addAttribute("kelulusan", kelulusan.get(0));
			model.addAttribute("persentase", new DecimalFormat("##.##").format(persentaseKelulusan));
			model.addAttribute("totalMahasiswa", jumlahMahasiswa);
			model.addAttribute("totalMahasiswaLulus", jumlahMahasiswaLulus);
			return "persentase-kelulusan";
		} else {
			return "data-notfound";
		}
	}
	
	@RequestMapping("/mahasiswa/cari")
    public String cariMahasiswa(Model model,
                                @RequestParam(value = "univ", required = false) Optional<String> univ,
                                @RequestParam(value = "fakultas", required = false) Optional<String> fakultas,
                                @RequestParam(value = "prodi", required = false) Optional<String> prodi)
    {

        if (univ.isPresent() && fakultas.isPresent() && prodi.isPresent()){
            List<StudentDataTableModel> students = studentDAO.selectStudentDataTable (Integer.parseInt(univ.get()), Integer.parseInt(fakultas.get()), Integer.parseInt(prodi.get()));
            model.addAttribute("univ", univ.get());
            model.addAttribute("fakultas", fakultas.get());
            model.addAttribute("prodi", prodi.get());
            model.addAttribute("students", students);
            model.addAttribute("student", students.get(0));
            return "viewall";
        }else{
        	
        	
            return "cari-mahasiswa";
        }
    }
	
	
	@RequestMapping(value="/mahasiswa/select/universitas", method=RequestMethod.GET)
    public ResponseEntity<List<UniversitasModel>> selectUniversitas(Model model, @RequestParam(value="univ", required = false) String  univ)
    {
        List<UniversitasModel> university = studentDAO.getAllUniv();
        
        return new ResponseEntity<List<UniversitasModel>>(university, HttpStatus.OK);
    }

    @RequestMapping(value="/mahasiswa/select/fakultas", method=RequestMethod.GET)
    public ResponseEntity<List<FakultasModel>> selectFaculty(Model model, @RequestParam(value="univ", required = false) String  univ)
    {
    	System.out.println("oke"+univ);
    	
        List<FakultasModel> faculty = studentDAO.selectFakultasByUniv(Integer.parseInt(univ));

        return new ResponseEntity<List<FakultasModel>>(faculty, HttpStatus.OK);
    }

    @RequestMapping(value="/mahasiswa/select/studiprogram", method=RequestMethod.GET)
    public ResponseEntity<List<ProdiModel>> selectStudyProgram(Model model, @RequestParam(value="fakultas", required = false) Optional<String>  fakultas)
    {
        if(fakultas.isPresent())
        {
            List<ProdiModel> studyProgram = studentDAO.selectProdiByFakultas(Integer.valueOf(fakultas.get()));
            return new ResponseEntity<List<ProdiModel>>(studyProgram, HttpStatus.OK);
        } else {
            List<ProdiModel> studyProgram = studentDAO.selectAllProdi();
            return new ResponseEntity<List<ProdiModel>>(studyProgram, HttpStatus.OK);
        }
    }
	
	@RequestMapping("/student/viewall")
	public String view(Model model) {
//		List<StudentModel> students = studentDAO.selectAllStudents();
//		model.addAttribute("students", students);

		return "viewall";
	}

	@RequestMapping("/student/delete/{npm}")
	public String delete(Model model, @PathVariable(value = "npm") String npm) {
		StudentModel student = studentDAO.selectStudent(npm);

		if (student != null) {
//			studentDAO.deleteStudent(npm);
			return "delete";
		} else {
			model.addAttribute("npm", npm);
			return "not-found";
		}
	}

	@RequestMapping("/mahasiswa/ubah/{npm}")
	public String update(Model model, @PathVariable(value = "npm") String npm) {

		StudentModel student = studentDAO.selectStudent(npm);
		if (student != null) {
			model.addAttribute("student", student);
			System.out.println("Student ID: " + student.getId());
			return "form-update";
		} else
			return "not-found";
	}

	@RequestMapping("/mahasiswa/ubah/submit")
	public String updateSubmit(Model model, @ModelAttribute("student") StudentModel student) {
		ProdiModel prodi = studentDAO.selectProdi(student.getId_prodi());
		FakultasModel fakultas = studentDAO.selectFakultas(prodi.getId_fakultas());
		UniversitasModel univ = studentDAO.selectUniv(fakultas.getId_univ());
		// String lastNPMInserted =
		// studentDAO.getLastNPMInserted(student.getId_prodi());

		int kodeJalurMasuk;
		String jalurMasuk;
		switch (student.getJalur_masuk()) {
		case "UndanganOlimpiade":
			kodeJalurMasuk = 53;
			jalurMasuk = "Undangan Olimpiade";
			break;
		case "UndanganRegulerSNMPTN":
			kodeJalurMasuk = 54;
			jalurMasuk = "Undangan Reguler / SNMPTN";
			break;
		case "UndanganParalelPPKB":
			kodeJalurMasuk = 55;
			jalurMasuk = "Undangan Paralel / PPKB";
			break;
		case "UjianTulisBersamaSBMPTN":
			kodeJalurMasuk = 57;
			jalurMasuk = "Ujian Tulis Bersama / SBMPTN";
			break;
		case "UjianTulisMandiri":
			kodeJalurMasuk = 62;
			jalurMasuk = "Ujian Tulis Mandiri";
			break;
		default:
			kodeJalurMasuk = 0;
			jalurMasuk = "";
			break;
		}
		String tahunMasuk = student.getTahun_masuk().substring(2, student.getTahun_masuk().length());
		int kodeUniv = Integer.parseInt(univ.getKode_univ());
		int kodeProdi = Integer.parseInt(prodi.getKode_prodi());
		int lastInsertId = 128;
		String npm = tahunMasuk + kodeUniv + kodeProdi + kodeJalurMasuk + lastInsertId;

		student.setNpm(npm);
		student.setJalur_masuk(jalurMasuk);

		// if (bindingResult.hasErrors()) {
		// return "form-update";
		// } else {
		if (studentDAO.updateStudent(student)) {
			model.addAttribute("student", student);
			return "success-update";
		} else {
			return "form-update";
		}

		// }

	}

}

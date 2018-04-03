package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.StudentMapper;
import com.example.model.FakultasModel;
import com.example.model.KelulusanModel;
import com.example.model.ProdiModel;
import com.example.model.StudentDataTableModel;
import com.example.model.StudentModel;
import com.example.model.UniversitasModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceDatabase implements StudentService
{
    @Autowired
    private StudentMapper studentMapper;


    public StudentServiceDatabase(StudentMapper studentMapper2) {
		// TODO Auto-generated constructor stub
    	this.studentMapper = studentMapper2;
	}


	public StudentServiceDatabase() {
		// TODO Auto-generated constructor stub
	}


	@Override
    public StudentModel selectStudent (String npm)
    {
        log.info ("select student with npm {}", npm);
        return studentMapper.selectStudent (npm);
    }


    @Override
    public boolean addStudent (StudentModel student)
    {
        return studentMapper.addStudent (student);
    }

    
    @Override
    public boolean updateStudent (StudentModel student)
    {
    	log.info("student " + student.getNpm() + " updated");
    	return studentMapper.updateStudent(student);
 
    }


	@Override
	public ProdiModel selectProdi(int id) {
		// TODO Auto-generated method stub
		return studentMapper.selectProdi(id);
	}


	@Override
	public FakultasModel selectFakultas(int id) {
		// TODO Auto-generated method stub
		return studentMapper.selectFakultas(id);
	}


	@Override
	public UniversitasModel selectUniv(int id) {
		// TODO Auto-generated method stub
		return studentMapper.selectUniv(id);
	}


	@Override
	public String getLastNPMInserted(int id_prodi) {
		// TODO Auto-generated method stub
		return studentMapper.getLastNPMInserted(id_prodi);
	}


	@Override
	public List<KelulusanModel> getKelulusan(int tahun, int id_prodi) {
		// TODO Auto-generated method stub
		return studentMapper.getKelulusan(tahun, id_prodi);
	}


	@Override
	public List<UniversitasModel> getAllUniv() {
		// TODO Auto-generated method stub
		return studentMapper.selectAllUniv();
	}


	@Override
	public List<FakultasModel> selectFakultasByUniv(int id_univ) {
		// TODO Auto-generated method stub
		return studentMapper.selectFakultasByUniv(id_univ);
	}


	@Override
	public List<ProdiModel> selectProdiByFakultas(int id_fakultas) {
		// TODO Auto-generated method stub
		return studentMapper.selectProdiByFakultas(id_fakultas);
	}


	@Override
	public List<ProdiModel> selectAllProdi() {
		// TODO Auto-generated method stub
		return studentMapper.selectAllProdi();
	}


	@Override
	public List<StudentDataTableModel> selectStudentDataTable(int univ, int fakultas, int prodi) {
		// TODO Auto-generated method stub
		return studentMapper.selectStudentDataTable(univ, fakultas, prodi);
	}
	

}

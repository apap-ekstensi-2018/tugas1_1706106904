package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import com.example.model.FakultasModel;
import com.example.model.KelulusanModel;
import com.example.model.ProdiModel;
import com.example.model.StudentDataTableModel;
import com.example.model.StudentModel;
import com.example.model.UniversitasModel;

@Mapper
public interface StudentMapper
{
    @Select("select * from mahasiswa as M where npm = #{npm} order by M.npm DESC LIMiT 1")
    StudentModel selectStudent (@Param("npm") String npm);

    @Select("select kode_prodi, nama_prodi, id_fakultas from program_studi where id = #{id}")
    ProdiModel selectProdi (@Param("id") int id);
    
    @Select("select nama_fakultas, id_univ from fakultas where id = #{id}")
    FakultasModel selectFakultas(@Param("id") int id);
    
    @Select("select * from fakultas where id_univ=#{id_univ}")
    List<FakultasModel> selectFakultasByUniv(@Param("id_univ") int id_univ);
    
    @Select("select * from program_studi where id_fakultas=#{id_fakultas}")
    List<ProdiModel> selectProdiByFakultas(@Param("id_fakultas") int id_fakultas);
    
    @Select("select * from program_studi")
    List<ProdiModel> selectAllProdi ();
    
    @Select("SELECT M.npm, M.nama, M.tahun_masuk, M.jalur_masuk, PS.nama_prodi, F.nama_fakultas, U.nama_univ " + 
    		"FROM mahasiswa AS M, program_studi AS PS, fakultas AS F, universitas AS U " + 
    		"WHERE M.id_prodi = PS.id " + 
    		"AND PS.id = #{prodi} " + 
    		"AND M.id_prodi = PS.id " + 
    		"AND PS.id_fakultas = F.id " + 
    		"AND F.id_univ = U.id")
    List<StudentDataTableModel> selectStudentDataTable(@Param("univ") int univ,
                                                       @Param("fakultas") int fakultas,
                                                       @Param("prodi") int prodi);

    
    @Select("select kode_univ, nama_univ from universitas where id = #{id}")
    UniversitasModel selectUniv(@Param("id") int id);
    
    @Select("select * from universitas")
    List<UniversitasModel> selectAllUniv();
    
    @Select("SELECT M.npm " + 
    		"FROM mahasiswa AS M, program_studi AS PS, fakultas AS F, universitas AS U " + 
    		"WHERE M.id_prodi = #{id_prodi} AND M.id_prodi = PS.id AND PS.id_fakultas = F.id " + 
    		"AND F.id_univ = U.id " + 
    		"ORDER BY M.npm DESC " + 
    		"LIMIT 1")
    String getLastNPMInserted(@Param("id_prodi") int id_prodi);
    
    @Select("SELECT M.tahun_masuk, PS.nama_prodi, F.nama_fakultas, U.nama_univ, M.status, COUNT(M.status) AS jumlah " + 
    		"FROM mahasiswa AS M, program_studi AS PS, fakultas AS F, universitas AS U " + 
    		"WHERE M.tahun_masuk = #{tahun_masuk} " + 
    		"AND PS.id = #{id} " + 
    		"AND M.id_prodi = PS.id " + 
    		"AND PS.id_fakultas = F.id " + 
    		"AND F.id_univ = U.id " + 
    		"GROUP BY M.status")
    List<KelulusanModel> getKelulusan(@Param("tahun_masuk") int tahun_masuk, @Param("id") int id);
    

    @Insert("INSERT INTO mahasiswa (npm, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, "
    		+ "agama, golongan_darah, tahun_masuk, "
    		+ "jalur_masuk, id_prodi) VALUES (#{npm}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, "
    		+ "#{jenis_kelamin}, #{agama}, #{golongan_darah}, #{tahun_masuk}, #{jalur_masuk}, "
    		+ "#{id_prodi})")
    boolean addStudent (StudentModel student);
    
    
    @Update("UPDATE mahasiswa SET npm = #{npm}, "
    		+ "nama = #{nama}, "
    		+ "tempat_lahir = #{tempat_lahir}, "
    		+ "tanggal_lahir = #{tanggal_lahir}, "
    		+ "jenis_kelamin = #{jenis_kelamin}, "
    		+ "agama = #{agama}, "
    		+ "golongan_darah = #{golongan_darah}, "
    		+ "status = #{status}, "
    		+ "tahun_masuk = #{tahun_masuk}, "
    		+ "jalur_masuk = #{jalur_masuk}, "
    		+ "id_prodi = #{id_prodi} "
    		+ "WHERE id = #{id}")
    boolean updateStudent(StudentModel student);
}

package com.example.service;

import java.util.List;

import com.example.model.FakultasModel;
import com.example.model.KelulusanModel;
import com.example.model.ProdiModel;
import com.example.model.StudentDataTableModel;
import com.example.model.StudentModel;
import com.example.model.UniversitasModel;

public interface StudentService
{
    StudentModel selectStudent (String npm);

    boolean addStudent (StudentModel student);
    
    boolean updateStudent(StudentModel student);
    
    ProdiModel selectProdi(int id);
    
    FakultasModel selectFakultas(int id);
    
    UniversitasModel selectUniv(int id);
    
    String getLastNPMInserted(int id_prodi);
    
    List<KelulusanModel> getKelulusan(int tahun, int id_prodi);
    
    List<UniversitasModel> getAllUniv();
    
    List<FakultasModel> selectFakultasByUniv(int id_univ);
    
    List<ProdiModel> selectProdiByFakultas(int id_fakultas);
    
    List<ProdiModel> selectAllProdi();
    
    List<StudentDataTableModel> selectStudentDataTable(int univ, int fakultas, int prodi);

    
    
    
}

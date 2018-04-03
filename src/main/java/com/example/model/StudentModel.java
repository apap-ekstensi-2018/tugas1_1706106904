package com.example.model;


import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentModel
{	
	private int id;
	
	@NotNull
	@Size(min=2, max=12)
    private String npm;
    
	@NotNull
	@Size(min=2, max=30)
    private String nama;
	
    private String tempat_lahir;
    private String tanggal_lahir;
	private String jenis_kelamin;
	private String agama;
	private String golongan_darah;
	private String status;
	private String tahun_masuk;
	private String jalur_masuk;
	private int id_prodi;

}

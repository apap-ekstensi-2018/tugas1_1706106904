package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KelulusanModel {
	private int tahun_masuk;
	private String nama_prodi;
	private String nama_fakultas;
	private String nama_univ;
	private String status;
	private int jumlah;
}

package com.bankapp.owner.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendmanagementDto {

	private String receiver;
	private int sendAmount;
	private String sender;
	private String menegement;
	private Timestamp createDate;
}

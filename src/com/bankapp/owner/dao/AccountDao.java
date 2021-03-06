package com.bankapp.owner.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bankapp.owner.db.DBConn;
import com.bankapp.owner.dto.MempoolBlock;
import com.bankapp.owner.dto.SendLogDto;
import com.bankapp.owner.dto.SendmanagementDto;
import com.bankapp.owner.model.Account;
import com.bankapp.owner.model.CheckSend;
import com.google.gson.Gson;



public class AccountDao {

	private static final String TAG = "AccountDao : ";
	private static AccountDao instance = new AccountDao();
	private AccountDao() {}
	public static AccountDao getinstance() {
		return instance;
	}
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public int findbyphone(String phone) {
		final String SQL = "select count(*) from account where phone = ?";
		
		try {
		conn = DBConn.getConnection();
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, phone);
		//물음표 완성하기
		rs=pstmt.executeQuery();
		if (rs.next()) {
				return rs.getInt(1);
		}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(TAG+"findbyphone : "+e.getMessage());
		}finally {
			DBConn.close(conn, pstmt);
		}
		return -1;
	}
	
	
	public CheckSend checkPhone(String receiver) {
		final String SQL = "select phone from account where phone = ?";
		CheckSend checkSend = null;
		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(SQL);
			//물음표 완성
			pstmt.setString(1, receiver);
			
			rs = pstmt.executeQuery();		
			if(rs.next()) {
				checkSend = new CheckSend();
				checkSend.setReceiver(rs.getString(1));
			}
			return checkSend;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(TAG+"checkPhone : "+e.getMessage());
		}finally {
			DBConn.close(conn, pstmt,rs);
		}
		return null;
	}
	
	public Account pwdConfirm(String pwd, String phone) {
		final String SQL = "select id,name,pwd,phone,amount from account where pwd = ? and phone = ?";
			Account account = null;
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, pwd);
				pstmt.setString(2, phone);
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					account = new Account();
					account.setId(rs.getInt("id"));
					account.setName(rs.getString("name"));
					account.setPwd(rs.getString("pwd"));
					account.setPhone(rs.getString("phone"));
					account.setAmount(rs.getInt("amount"));
				}
				return account;
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"pwdConfirm : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt,rs);
			}
			return null;
		}
	
	public int insertBlockList (String hash, String previousHash, String data,String timestamp,int nonce,int nodeNumber) {
		final String SQL = "insert into blocklist(id,hash,previoushash,data,timestamp,nonce,nodeNum) values(blocklist_seq.nextval,?,?,?,?,?,?)";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, hash);
				pstmt.setString(2, previousHash);
				pstmt.setString(3, data);
				pstmt.setString(4, timestamp);
				pstmt.setInt(5, nonce);
				pstmt.setInt(6, nodeNumber);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"insertBlockMempool : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	
	
	public String sendBlockData() {
			final String SQL = "select id,receiver,sendAmount,sender,menegement,hash,createDate from blockmempool";
			List<MempoolBlock> mempoolBlocks = new ArrayList<MempoolBlock>();
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					MempoolBlock mempoolBlock = MempoolBlock.builder()
							.id(rs.getInt(1))
							.receiver(rs.getString(2))
							.sendAmount(rs.getInt(3))
							.sender(rs.getString(4))
							.menegement(rs.getString(5))
							.hash(rs.getString(6))
							.createDate(rs.getTimestamp(7))
							.build();
					mempoolBlocks.add(mempoolBlock);
				}
				Gson gson = new Gson();
				String mempoolBlocksJson = gson.toJson(mempoolBlocks);
				
				return mempoolBlocksJson;
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"sendBlockData : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt,rs);
			}
			return null;
		}
	
	public List<SendLogDto> sendLog(String phone) {
		final String SQL = "select receiver,sendAmount,sender,createDate from mempool where sender = ? or receiver = ? order by id desc";
			List<SendLogDto> sendLogDtos = new ArrayList<SendLogDto>();
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, phone);
				pstmt.setString(2, phone);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					SendLogDto sendLogDto = SendLogDto.builder()
							.receiver(rs.getString(1))
							.sendAmount(rs.getInt(2))
							.sender(rs.getString(3))
							.createDate(rs.getTimestamp(4))
							.build();
					sendLogDtos.add(sendLogDto);
				}
				return sendLogDtos;
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"sendLog : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt,rs);
			}
			return null;
		}
	public List<SendmanagementDto> sendLogmenegement(String management) {
		final String SQL = "select receiver,sendAmount,sender,menegement,createDate from mempool where menegement= ? order by id desc";
		List<SendmanagementDto> sendmanagementDtos = new ArrayList<SendmanagementDto>();
		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(SQL);
			//물음표 완성
			pstmt.setString(1, management);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				SendmanagementDto sendmanagementDto = SendmanagementDto.builder()
						.receiver(rs.getString(1))
						.sendAmount(rs.getInt(2))
						.sender(rs.getString(3))
						.menegement(rs.getString(4))
						.createDate(rs.getTimestamp(5))
						.build();
				sendmanagementDtos.add(sendmanagementDto);
			}
			return sendmanagementDtos;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(TAG+"sendLog : "+e.getMessage());
		}finally {
			DBConn.close(conn, pstmt,rs);
		}
		return null;
	}
	
	public int deleteBlockMempool() {
		final String SQL = "delete from blockmempool";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성

				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"deleteBlockMempool : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	
	public int insertBlockMempool (String receiver, int sendAmount, String phone,String menegement,String hash) {
		final String SQL = "insert into blockmempool(id,receiver,sendAmount,sender,menegement,hash,createDate) values(blockmempool_seq.nextval,?,?,?,?,?,sysdate)";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, receiver);
				pstmt.setInt(2, sendAmount);
				pstmt.setString(3, phone);
				pstmt.setString(4, menegement);
				pstmt.setString(5, hash);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"insertBlockMempool : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	
	public int insertMempool (String receiver, int sendAmount, String phone,String menegement, String hash) {
		final String SQL = "insert into mempool(id,receiver,sendAmount,sender,menegement,hash,createDate) values(mempool_seq.nextval,?,?,?,?,?,sysdate)";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, receiver);
				pstmt.setInt(2, sendAmount);
				pstmt.setString(3, phone);
				pstmt.setString(4, menegement);				
				pstmt.setString(5, hash);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"insertMempool : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	
	public int recevie(int amount, String phone) {
		final String SQL = "update account set amount =amount+? where phone = ? ";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setInt(1, amount);
				pstmt.setString(2, phone);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"recevie : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	public int send(int amount, String phone) {
		final String SQL = "update account set amount = amount-? where phone = ? ";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setInt(1, amount);
				pstmt.setString(2, phone);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"send : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	
	
	
	
	
	
	public int join(Account account) {
	final String SQL = "insert into account(id,name,pwd,menegement,phone,amount) values(account_seq.nextval,?,?,?,?,0)";
		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(SQL);
			//물음표 완성
			pstmt.setString(1, account.getName());
			pstmt.setString(2, account.getPwd());
			pstmt.setString(3, account.getMenegement());
			pstmt.setString(4, account.getPhone());
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(TAG+"join : "+e.getMessage());
		}finally {
			DBConn.close(conn, pstmt);
		}
		return -1;
	}
	public Account login(String name, String pwd) {
		final String SQL = "select id,name,phone,menegement,amount from account where name = ? and pwd = ?";
			Account account = null;
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, name);
				pstmt.setString(2, pwd);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					account = new Account();
					account.setId(rs.getInt("id"));
					account.setName(rs.getString("name"));
					account.setPhone(rs.getString("phone"));
					account.setMenegement(rs.getString("menegement"));
					account.setAmount(rs.getInt("amount"));
				}
				return account;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"login : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt,rs);
			}
			return null;
		}
	public int charge(int amount, String phone) {
		final String SQL = "update account set amount =amount+? where phone = ? ";
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setInt(1, amount);
				pstmt.setString(2, phone);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"charge : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt);
			}
			return -1;
		}
	public Account find(String phone) {
		final String SQL = "select id,name,phone,amount from account where phone = ?";
			Account account = null;
			try {
				conn = DBConn.getConnection();
				pstmt = conn.prepareStatement(SQL);
				//물음표 완성
				pstmt.setString(1, phone);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					account = new Account();
					account.setId(rs.getInt("id"));
					account.setName(rs.getString("name"));
					account.setPhone(rs.getString("phone"));
					account.setAmount(rs.getInt("amount"));
				}
				return account;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(TAG+"find : "+e.getMessage());
			}finally {
				DBConn.close(conn, pstmt,rs);
			}
			return null;
		}
}

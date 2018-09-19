package com.ao.scanCommunicate.auth;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.ao.scanElectricityBis.auth.CurrentUserAware;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal.UserType;

/**
 * ע��ͨѶ�������Ĺ������ʺ�
 * @author aohanhe
 *
 */
@Service
public class ScanServerCurrentUserAware implements CurrentUserAware {
	
	private static ScanServerPrincipal curUser;
	
	@PostConstruct
	public void init()
	{
		curUser=new ScanServerPrincipal();
		curUser.setAdminUser(true);
		curUser.setName("ͨѶ������");
		curUser.setOpenId("");
		curUser.setOperatorId(-1);
		curUser.setOperatorName("");
		curUser.setPhone("");
		curUser.setRightAreaCode("");
		curUser.setUser(0);
		curUser.setUserType(UserType.MangerUser);
	}

	@Override
	public ScanServerPrincipal currentUser() {
		
		return curUser;
	}

}

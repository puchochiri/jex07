package org.zerock.mapper;

import org.zerock.security.domain.MemberVO;

public interface MemberMapper {
	
	public MemberVO read(String userid);

}

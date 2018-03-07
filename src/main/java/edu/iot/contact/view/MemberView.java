package edu.iot.contact.view;

import java.util.List;

import edu.iot.contact.model.Contact;
import edu.iot.contact.model.Member;
import edu.iot.lib.view.AbstractView;

public class MemberView extends AbstractView {
	// Singletone 패턴
	private MemberView() {

	}

	private static MemberView view = new MemberView();

	public static MemberView getInstance() {
		return view;
	}

	/*전체 목록에서 선택한 페이지를 보여줌*/
	public void printPage(List<Member> list, 
			int start, int page, int totalPage, int total){
		System.out.println("사용자ID       이름        전화번호              이메일        GRADE");
		System.out.println("-----------------------------------------------------");
		for(Member m : list) {
			System.out.printf("%8s %8s %14s %s %d%n",
					m.getUserId(), m.getName(),
					m.getCellPhone(), m.getEmail(), m.getGrade());
		}
		System.out.println("-----------------------------------------------------");
		System.out.printf("%d/%d(총 %d건)\n", page, totalPage, total);
	}
	
	public Member getNewMember(String userId) {
		Member member = new Member();
		member.setUserId(userId);
		member.setName(view.getString("사용자 이름: "));
		member.setPassword(view.getString("비밀번호: "));
		member.setCellPhone(view.getString("전화번호: "));
		member.setEmail(view.getString("Email: "));
		member.setAddress(view.getString("주소: "));

		return member;
	}
	
	public Member getUpdatedMember(Member member) {

		String cellPhone = getString(String.format("전화번호 (%s): ", member.getCellPhone()));
		if(!cellPhone.isEmpty()) {
			member.setCellPhone(cellPhone);
		}
		
		String email = getString(String.format("email (%s): ", member.getEmail()));
		if(!email.isEmpty()) {
			member.setEmail(email);
		}
		
		String address = getString(String.format("주소 (%s): ", member.getAddress()));
		if(!address.isEmpty()) {
			member.setAddress(address);
		}
		
		return member;
	}
}

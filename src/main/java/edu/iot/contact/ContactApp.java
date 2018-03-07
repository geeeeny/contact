package edu.iot.contact;

import edu.iot.contact.model.Member;
import edu.iot.contact.service.ContactService;
import edu.iot.contact.service.ContactServiceImp1;
import edu.iot.contact.service.MemberService;
import edu.iot.contact.service.MemberServiceImp1;
import edu.iot.lib.app.Application;
import edu.iot.lib.app.Context;
import edu.iot.lib.db.ConnectionProvider;
import edu.iot.lib.view.MenuBar;
import edu.iot.lib.view.MenuGroup;
import edu.iot.lib.view.MenuItem;
import edu.iot.lib.view.View;

public class ContactApp extends Application {

	MenuBar guestMenu;	//게스트 메뉴
	MenuBar adminMenu;	//관리자 메뉴
	MenuBar memberMenu;	//회원 메뉴
	
	MemberService memberService;
	ContactService contactService;
	
	public ContactApp() {
		memberService = new MemberServiceImp1();
		contactService = new ContactServiceImp1();
	}

	@Override
	public void init() {
		super.init();	//createMenu() 호출
		mainMenu = guestMenu; //처음 시작시 게스트 메뉴를 보여줌 
		//mainMenu = adminMenu; //처음 시작시 관리자 메뉴를 보여줌 
	}

	@Override
	public void createMenu() {
		createGuestMenu();
		createMemberMenu();
		createAdminMenu();
	}
	
	public void createGuestMenu() {
		guestMenu = new MenuBar();
		
		guestMenu.add(new MenuItem("로그인", this::login));
		guestMenu.add(new MenuItem("회원가입", memberService::join));
		guestMenu.add(new MenuItem("종료", this::exit));
	}
	
	public void createMemberMenu() {
		memberMenu = new MenuBar();
		
		MenuGroup contactMenu = new MenuGroup("연락처 관리");
		contactMenu.add(new MenuItem("목록", contactService::printList));
		contactMenu.add(new MenuItem("검색", contactService::search));
		contactMenu.add(new MenuItem("추가", contactService::add));
		contactMenu.add(new MenuItem("수정", contactService::update));
		contactMenu.add(new MenuItem("삭제", contactService::delete));
		memberMenu.add(contactMenu);
		
		memberMenu.add(new MenuItem("비밀번호 변경", null));
		memberMenu.add(new MenuItem("회원정보 수정", null));
		memberMenu.add(new MenuItem("로그아웃", this::logout));
		memberMenu.add(new MenuItem("종료", this::exit));
	}
	
	public void createAdminMenu() {
		adminMenu = new MenuBar();
		
		MenuGroup memberMenu = new MenuGroup("회원관리");
		memberMenu.add(new MenuItem("회원 목록", memberService::printList));
		memberMenu.add(new MenuItem("회원 추가", memberService::join));
		memberMenu.add(new MenuItem("회원 수정", memberService::update));
		memberMenu.add(new MenuItem("회원 삭제", memberService::delete));
		adminMenu.add(memberMenu);
		
		MenuGroup contactMenu = new MenuGroup("연락처 관리");
		contactMenu.add(new MenuItem("전체 목록", contactService::printList));
		contactMenu.add(new MenuItem("회원별 목록", contactService::printPerOwner));
		adminMenu.add(contactMenu);
		
		adminMenu.add(new MenuItem("로그아웃", this::logout));
		adminMenu.add(new MenuItem("종료", this::exit));
	}
	
	public void login() {
		try {
			Member member = memberService.login();
			if(member!=null) {	//로그인 성공
				Context.setAttribute("USER", member);
				if(member.getGrade()==0) {
					mainMenu = adminMenu;
				}else {
					mainMenu = memberMenu;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logout() {
		Context.remove("USER");
		mainMenu = guestMenu;
	}

	@Override
	public void exit() {
		ConnectionProvider.close();
		super.exit();
	}

}

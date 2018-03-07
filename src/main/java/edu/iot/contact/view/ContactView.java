package edu.iot.contact.view;

import java.util.List;

import edu.iot.contact.model.Contact;
import edu.iot.lib.view.AbstractView;

public class ContactView extends AbstractView {
	// Singletone 패턴
	private ContactView() {

	}

	private static ContactView view = new ContactView();

	public static ContactView getInstance() {
		return view;
	}

	/*접근할 수 있는 전체 목록에서 선택한 페이지를 보여줌*/
	public void printPage(List<Contact> list, 
			int start, int page, int totalPage, int total){
		printList(list);
		System.out.printf("%d/%d(총 %d건)\n", page, totalPage, total);
	}
	
	public void printList(List<Contact> list) {
		System.out.println("  No 	     이름        전화번호              이메일");
		System.out.println("----------------------------------------------");
		for(Contact c : list) {
			System.out.printf("%3d) %8s %14s %s%n",
					c.getContactId(), c.getName(),
					c.getCellPhone(), c.getEmail());
		}
		System.out.println("----------------------------------------------");
	}
	
	public Contact getNewContact(String owner) {
		Contact contact = new Contact();
		contact.setOwner(owner);
		contact.setName(getString("이름: "));
		contact.setCellPhone(getString("전화번호: "));
		contact.setEmail(getString("email: "));
		contact.setAddress(getString("주소: "));
		return contact;
	}
	
	public Contact getUpdatedContact(Contact contact) {
		String name = getString(String.format("이름 (%s): ", contact.getName()));
		if(!name.isEmpty()) {
			contact.setName(name);
		}
		
		String cellPhone = getString(String.format("전화번호 (%s): ", contact.getCellPhone()));
		if(!cellPhone.isEmpty()) {
			contact.setCellPhone(cellPhone);
		}
		
		String email = getString(String.format("email (%s): ", contact.getEmail()));
		if(!email.isEmpty()) {
			contact.setEmail(email);
		}
		
		String address = getString(String.format("주소 (%s): ", contact.getAddress()));
		if(!address.isEmpty()) {
			contact.setAddress(address);
		}
		
		return contact;
	}
}

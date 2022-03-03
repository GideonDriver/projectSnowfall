package com.training.pms.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.training.bms.dao.LoginDAO;
import com.training.bms.dao.LoginDAOImpl;
import com.training.bms.model.Login;

class LoginDAOImplTest1 {
	LoginDAO loginDAO;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		loginDAO = new LoginDAOImpl();
	}

	@AfterEach
	void tearDown() throws Exception {
		loginDAO = null;
	}

	@Test
	void testUsernameTaken1() {
		String userName ="Admin";
		boolean actual = loginDAO.usernameTaken(userName);
		assertTrue(actual);
	}

	@Test
	void testUsernameTaken2() {
		String userName = "Zonks";
		boolean actual = loginDAO.usernameTaken(userName);
		assertFalse(actual);
	}

	@Test
	void testSeachByName1() {
		Login expected = new Login(90000000, "Admin", "adminpassword", "Employee", "Active");
		Login actual = loginDAO.seachByName("Admin", "Employee", "Active");
		assertEquals(actual, expected);
	}

	@Test
	void testSeachByName2() {
		Login expected = new Login(90000000, "Admin", "adminpassword", "Employee", "Active");
		Login actual = loginDAO.seachByName("Admin", null, null);
		assertEquals(actual, expected);
	}

	@Test
	void testSeachByName3() {
		Login expected = new Login(90000000, "Admin", "adminpassword1", "Employee", "Active");
		Login actual = loginDAO.seachByName("Admin", null, null);
		assertNotEquals(actual, expected);
	}

	@Test
	void testSeachByName4() {
		Login expected = new Login(90000000, "Admin", "adminpassword", "Employee", "Active");
		Login actual = loginDAO.seachByName("Admin1", null, null);
		assertNotEquals(actual, expected);
	}

	@Test
	void testSeachById1() {
		Login expected = new Login(90000000, "Admin", "adminpassword", "Employee", "Active");
		Login actual = loginDAO.seachById(90000000);
		assertEquals(actual, expected);
	}

	@Test
	void testSeachById2() {
		Login expected = new Login(90000000, "Admin", "adminpassword1", "Employee", "Active");
		Login actual = loginDAO.seachById(90000000);
		assertNotEquals(actual, expected);
	}

	@Test
	void testSeachById3() {
		Login expected = new Login(90000000, "Admin", "adminpassword", "Employee", "Active");
		Login actual = loginDAO.seachById(90000001);
		assertNotEquals(actual, expected);
	}

	@Test
	void testSeachById4() {
		Login expected = new Login(90000000, "Admin", "adminpassword", "Employee", "Active");
		Login actual = loginDAO.seachById(90000010);
		assertNotEquals(actual, expected);
	}

}

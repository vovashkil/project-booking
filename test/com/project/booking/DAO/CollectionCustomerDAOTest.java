package com.project.booking.DAO;

import com.project.booking.Booking.Customer;
import com.project.booking.Constants.Sex;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.project.booking.Constants.ComUtil.parseDate;

public class CollectionCustomerDAOTest {
    private final String TEST_FILE_PATH = "./files/db/test.db";
    private PersonDAO customerDAO;

    private Customer customer1_1 = new Customer("Petro", "Ivanov", parseDate("09/09/1967"), Sex.MALE, "ivanov@i.ua", "asQW12#$");
    private Customer customer1_2 = new Customer("Masha", "Dudnik", parseDate("23/01/1988"), Sex.FEMALE, "dudnik_masha@i.ua", "zxAS12#$");

    @Before
    public void setUp() throws Exception {
        customerDAO = new CollectionCustomerDAO();
    }

    @After
    public void tearDown() throws Exception {
        customer1_1 = null;
        customer1_2 = null;
        customerDAO = null;
    }

    @Test
    public void checkGetAllCustomers() {
        //when
        customerDAO.save(customer1_1);
        customerDAO.save(customer1_2);
        int expectedCountCustomers = 2;
        //then
        Assertions.assertThat(expectedCountCustomers).isEqualTo(customerDAO.getAll().size());
    }

    @Test
    public void checkSaveCustomer() {
        //when
        customerDAO.save(customer1_1);
        customerDAO.save(customer1_2);
        int expectedCountCustomers = 2;
        //then
        Assertions.assertThat(expectedCountCustomers).isEqualTo(customerDAO.getAll().size());
        Assertions.assertThat(customer1_2).isEqualTo(customerDAO.getByIndex(customerDAO.getAll().size() - 1));
    }

    @Test
    public void checkDeleteCustomerByIndex() {
        //when
        customerDAO.save(customer1_1);
        customerDAO.save(customer1_2);
        int expectedCountCustomers = 1;
        customerDAO.remove(-100);
        customerDAO.remove(100);
        customerDAO.remove(0);
        //then
        Assertions.assertThat(expectedCountCustomers).isEqualTo(customerDAO.getAll().size());
        Assertions.assertThat(customer1_2).isEqualTo(customerDAO.getByIndex(0));
    }

    @Test
    public void checkDeleteCustomerByObject() {
        //when
        customerDAO.save(customer1_1);
        customerDAO.save(customer1_2);
        int expectedCountCustomers = 1;
        customerDAO.remove(null);
        customerDAO.remove(customer1_1);
        //then
        Assertions.assertThat(expectedCountCustomers).isEqualTo(customerDAO.getAll().size());
        Assertions.assertThat(customer1_2).isEqualTo(customerDAO.getByIndex(0));
    }

    @Test
    public void checkSaveData() throws IOException {
        //when
        customerDAO.save(customer1_1);
        customerDAO.save(customer1_2);
        customerDAO.saveData(TEST_FILE_PATH);
        //then
        try {
            Assertions.assertThat(Files.exists(Paths.get(TEST_FILE_PATH)));
        } finally {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        }
    }

    @Test
    public void checkReadData() {
    }

    @Test
    public void checkLoadData() {
    }
}
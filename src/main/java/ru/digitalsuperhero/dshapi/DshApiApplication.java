package ru.digitalsuperhero.dshapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.digitalsuperhero.dshapi.dao.AdminRepository;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.WorkRequestRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Admin;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;
import ru.digitalsuperhero.dshapi.dao.domain.WorkRequest;

@SpringBootApplication
//@ComponentScan({"ru.digitalsuperhero.dshapi"})
public class DshApiApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(DshApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner adminLoader(AdminRepository adminRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (adminRepository.findByUsername("network") == null) {
                    adminRepository.save(new Admin("network", passwordEncoder.encode("network")));
                }
            }
        };
    }

    @Bean
    @Profile("!prod")
    public CommandLineRunner dataLoader(CustomerRepository customerRepository, ContractorRepository contractorRepository,
                                        WorkRequestRepository workRequestRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Customer customer1 = new Customer("test1", "test1@gmail.com", "+79991234567", "test");
                Customer customer2 =   new Customer("test2", "test2@gmail.com","+79991234567", "test");
                Customer customer3 = new Customer("test3", "test3@gmail.com","+79991234567", "test");
                customerRepository.save(customer1);
                customerRepository.save(customer2);
                customerRepository.save(customer3);
                customerRepository.save(new Customer("test4", "test4@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test5", "test5@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test6", "test6@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test7", "test7@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test8", "test8@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test9", "test9@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test10", "tes10@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test11", "test11@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test12", "test12@gmail.com", "+79991234567","test"));
                customerRepository.save(new Customer("test13", "test13@gmail.com","+79991234567", "test"));
                customerRepository.save(new Customer("test14", "test14@gmail.com","+79991234567", "test"));

                contractorRepository.save(new Contractor(0l, "test1", "test1@gmail.com", "test"));
                contractorRepository.save(new Contractor(1l, "test2", "test2@gmail.com", "test"));
                contractorRepository.save(new Contractor(2l, "test3", "test3@gmail.com", "test"));
                contractorRepository.save(new Contractor(3l, "test4", "test4@gmail.com", "test"));
                contractorRepository.save(new Contractor(4l, "test5", "test5@gmail.com", "test"));

                workRequestRepository.save(new WorkRequest(1l, "work1", "test", false, customer1, 1));
                workRequestRepository.save(new WorkRequest(2l, "work2", "test", false, customer2, 2));
                workRequestRepository.save(new WorkRequest(3l, "work3", "test", false, customer3, 3));
            }
        };
    }
}

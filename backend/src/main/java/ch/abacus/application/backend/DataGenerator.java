package ch.abacus.application.backend;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import ch.abacus.application.backend.CustomerRepositoryBean.CustomerEntity;

@Component
class DataGenerator {

    private final static String[] firstNames = { "Joonas", "Jurka", "Jani", "Marc", "Sami", "Henri", "Ville", "Mikael",
            "Tomi", "Arthur", "Jouni", "Matti", "Jonatan", "Hannu", "Henri", "Teemu", "Johannes", "Risto", "Kim",
            "Jens", "Henrik", "John", "Thomas", "Fredrik", "Teppo", "Sebastian", "Peter", "Tomi", "Petter", "Marcus",
            "Petri", "Anna", "Marlon", "Marko", "Leif", "Samuli", "Rolf", "Pekka", "Tapio", "Haijian", "Miki", "Tanja",
            "Jonni", "Dennis", "Amir", "Patrik", "Michael", "Joacim", "Felyppe", "Artem", "Mac", "Minna", "Sauli",
            "Manolo", "Enver", "Riitta", "Tiina", "Riikka" };

    private final static String[] lastNames = { "Lehtinen", "Rahikkala", "Laakso", "Englund", "Ekblad", "Muurimaa",
            "Ingman", "Vappula", "Virtanen", "Signell", "Koivuviita", "Tahvonen", "Salonen", "Kerola", "Tuikkala",
            "Leppänen", "Jansson", "Paul", "Ahlroos", "Mattson", "Kurki", "Sara", "Nyholm", "Lehto", "Virkki",
            "Holmström", "Hellberg", "Heinonen", "Vesa", "Koskinen", "Richert", "Grönroos", "Häyry", "Åstrand",
            "Kaksonen", "Repo" };

    @PostConstruct
    public Set<CustomerEntity> generateTestData() {
        Set<CustomerEntity> customers = new HashSet<>();

        IntStream.range(0, 1000).forEach(i -> {
            customers.add(generateCustomer());
        });

        return customers;
    }

    private CustomerEntity generateCustomer() {
        Random random = new Random();

        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        LocalDate birthDateCalc = LocalDate.now();
        birthDateCalc = birthDateCalc.withYear(1984).withMonth(1).withDayOfMonth(1);
        birthDateCalc = birthDateCalc.plusDays(random.nextInt(6000));

        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setBirthDate(birthDateCalc);
        customer.setCurrencyCode("EUR");
        customer.setPurchasesTotal(BigDecimal.valueOf(random.nextInt(1000)));

        return customer;
    }
}

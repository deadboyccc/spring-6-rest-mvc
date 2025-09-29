package dev.dead.spring6restmvc.playground;

import dev.dead.spring6restmvc.models.CustomerDTO;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class JavaUtilsOptionalUnderUtilized {

    public static void main(String[] args) {
        // Creation methods
        Optional<CustomerDTO> empty = Optional.empty();
        Optional<CustomerDTO> ofNullable = Optional.ofNullable(null);
        Optional<CustomerDTO> of = Optional.of(createCustomer("John"));

        // Basic operations 
        CustomerDTO customer = ofNullable.orElse(createCustomer("default"));
        // Lazy evaluation
        CustomerDTO customerSupplier = ofNullable.orElseGet(() -> createCustomer("supplier"));

        // Throwing exception if value not present
        try {
            CustomerDTO customerThrow = ofNullable.orElseThrow(() -> new RuntimeException("Customer not found"));
        } catch (RuntimeException e) {
            System.out.println("Exception handled: " + e.getMessage());
        }

        // Conditional operations
        Optional<CustomerDTO> filteredCustomer = of.filter(c -> c.getCustomerName()
                .startsWith("J"));

        // Transformation
        Optional<String> mappedName = of.map(CustomerDTO::getCustomerName);

        // Chaining operations
        String result = Optional.ofNullable(createCustomer("Alice"))
                .filter(c -> c.getCustomerName() != null)
                .map(CustomerDTO::getCustomerName)
                .map(String::toUpperCase)
                .orElse("UNKNOWN");

        // ifPresent and ifPresentOrElse
        of.ifPresent(c -> System.out.println("Customer found: " + c.getCustomerName()));

        ofNullable.ifPresentOrElse(
                c -> System.out.println("Found: " + c.getCustomerName()),
                () -> System.out.println("Not found")
        );

        // Stream operations
        List<CustomerDTO> customers = of.stream()
                .toList();

        // or() method (Java 9+)
        CustomerDTO alternativeCustomer = ofNullable
                .or(() -> Optional.of(createCustomer("alternative")))
                .orElse(createCustomer("default"));

        // Practical example combining multiple features
        processCustomer(null);
        processCustomer(createCustomer("Test"));
    }

    private static CustomerDTO createCustomer(String name) {
        return CustomerDTO.builder()
                .customerName(name)
                .build();
    }

    private static void processCustomer(CustomerDTO customer) {
        Optional.ofNullable(customer)
                .filter(c -> c.getCustomerName() != null && !c.getCustomerName()
                        .isEmpty())
                .map(c -> {
                    c.setCustomerName(c.getCustomerName()
                            .toUpperCase());
                    return c;
                })
                .ifPresentOrElse(
                        c -> System.out.println("Processed customer: " + c.getCustomerName()),
                        () -> System.out.println("No valid customer to process")
                );
    }
}
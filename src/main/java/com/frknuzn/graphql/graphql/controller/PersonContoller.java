package com.frknuzn.graphql.graphql.controller;



import com.frknuzn.graphql.graphql.entity.Person;
import com.frknuzn.graphql.graphql.repository.PersonRepository;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class PersonContoller {

    @Autowired
    private PersonRepository repository;

    @Value("classpath:person.graphqls")
    private Resource schemaResource;

    private GraphQL graphQL;

    @PostConstruct
    public void loadSchema() throws IOException {
        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<Person>> fetcher1 = data -> {
            return (List<Person>) repository.findAll();
        };

        DataFetcher<Person> fetcher2 = data -> {
            return repository.findByEmail(data.getArgument("email"));
        };

        DataFetcher<Person> fetcher3 = data -> {
            return repository.findByName(data.getArgument("name"));
        };

        return RuntimeWiring.newRuntimeWiring().type("Query",
                typeWriting -> typeWriting.dataFetcher("getAllPerson", fetcher1).dataFetcher("findPersonByEmail", fetcher2).dataFetcher("findByName",fetcher3))
                .build();

    }

    @PostMapping("/addPerson")
    public String addPerson(@RequestBody List<Person> persons) {
    repository.saveAll(persons);
        return "record inserted " + persons.size();
    }

    @GetMapping("/findAllPerson")
    public List<Person> getPersons() {
        return (List<Person>) repository.findAll();
    }

    @PostMapping("/getAll")
    public ResponseEntity<Object> getAll(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @PostMapping("/getPersonByEmail")
    public ResponseEntity<Object> getPersonByEmail(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

}

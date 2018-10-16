package com.piggymetrics.account.controller;

import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.repository.AccountServiceImp;
import com.piggymetrics.account.repository.Repository;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/")
public class AccountServiceEndpoint {

    @Inject
    private AccountServiceImp accountService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccounts() {
        Gson gson = new Gson();
        return gson.toJson(accountService.getAccounts());
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response response(User user) {
        System.out.println("HELLO" + user.getUsername() + "PASS" + user.getPassword());
        accountService.create(user);
        return Response.ok("Got the request okay").build();
    }

    @GET
    @Path("/current")
    @Produces("text/plain")
    public Response getCurrentAccount() {
        return Response.ok("Here is the current account!").build();
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public Response testDbConnection() {
        Gson gson = new Gson();
        MongoClient mongoClient = Repository.INSTANCE.getMongoClient();
        MongoDatabase db = mongoClient.getDatabase("piggymetrics");
        for (String name : db.listCollectionNames()) {
            System.out.println(name);
        }
        MongoCollection<Document> coll = db.getCollection("accounts");
        List<Account> accounts = new ArrayList<>();


        coll.find(new Document()).forEach((Block<? super Document>) document -> accounts.add(gson.fromJson(document.toJson(), Account.class)));

        for (Account a : accounts) {
            System.out.println("Here:" + a.getId());
        }

        //coll.find(new Document()).forEach(printBlock);
        //coll.find(new Document()).forEach(getAccounts());
        return Response.ok("Here is the current account!").build();
    }

    @GET
    @Path("/parse")
    @Produces("text/plain")
    public Response testParse() {
        MongoClient mongoClient = Repository.INSTANCE.getMongoClient();
        MongoDatabase db = mongoClient.getDatabase("piggymetrics");

        MongoCollection<Document> coll = db.getCollection("accounts");
        System.out.println(coll.getDocumentClass());

        return Response.ok("Here is the current account!").build();
    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

}

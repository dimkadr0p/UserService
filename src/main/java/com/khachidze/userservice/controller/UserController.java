package com.khachidze.userservice.controller;

import com.khachidze.userservice.dto.ResultMoneyTransferDto;
import com.khachidze.userservice.entity.UserEntity;
import com.khachidze.userservice.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/users")
public class UserController {

    @Inject
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Response createUser(UserEntity userEntity) {
        return Response.ok(userService.createUser(userEntity)).build();
    }

    @GET
    @Produces("application/json")
    public Response getUserByPhoneNumber(@QueryParam("phone") String phoneNumber) {
        Optional<UserEntity> user = userService.getUserByPhoneNumber(phoneNumber);
        if(user.isPresent())
            return Response.ok(user.get()).build();
        else
            return Response.status(Response.Status.NOT_FOUND).entity("User not found by phone").build();
    }

    @GET
    @Produces("application/json")
    @Path("/all")
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @PUT
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Response updateBalanceUser(ResultMoneyTransferDto resultMoneyTransferDto) {
        try {
            userService.updateBalanceUsers(resultMoneyTransferDto);
            return Response.ok("Transaction successful").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Transaction failed: " + e.getMessage()).build();
        }
    }

}
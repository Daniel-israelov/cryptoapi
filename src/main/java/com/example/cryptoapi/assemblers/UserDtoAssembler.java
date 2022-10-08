package com.example.cryptoapi.assemblers;

import com.example.cryptoapi.controllers.UserController;
import com.example.cryptoapi.dtos.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDtoAssembler implements SimpleRepresentationModelAssembler<UserDto> {

    @Override
    public void addLinks(EntityModel<UserDto> resource) {
        resource.add(linkTo(methodOn(UserController.class)
                .getUserByIdentityNumber(Objects.requireNonNull(resource.getContent()).getIdentityNumber()))
                .withSelfRel());
        resource.add(linkTo(methodOn(UserController.class)
                .getAllUsers()).withRel("all users"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDto>> resources) {
        resources.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }
}

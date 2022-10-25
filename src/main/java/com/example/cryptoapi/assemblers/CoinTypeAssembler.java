package com.example.cryptoapi.assemblers;

import com.example.cryptoapi.controllers.CoinTypeController;
import com.example.cryptoapi.entities.CoinTypeEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CoinTypeAssembler implements SimpleRepresentationModelAssembler<CoinTypeEntity> {

    @Override
    public void addLinks(EntityModel<CoinTypeEntity> resource) {
        resource.add(linkTo(methodOn(CoinTypeController.class)
                .getByName(Objects.requireNonNull(resource.getContent()).getName())).withSelfRel());
        resource.add(linkTo(methodOn(CoinTypeController.class)
                .getAllCoinTypes()).withRel("all cointypes"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<CoinTypeEntity>> resources) {
        resources.add(linkTo(methodOn(CoinTypeController.class).getAllCoinTypes()).withSelfRel());
    }
}

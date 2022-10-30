package com.example.cryptoapi.assemblers;

import com.example.cryptoapi.controllers.CoinController;
import com.example.cryptoapi.controllers.CoinTypeController;
import com.example.cryptoapi.controllers.WalletController;
import com.example.cryptoapi.dtos.CoinDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CoinDtoAssembler implements SimpleRepresentationModelAssembler<CoinDto> {

    @Override
    public void addLinks(EntityModel<CoinDto> resource) {
        resource.add(linkTo(methodOn(CoinController.class)
                .getByUUID(Objects.requireNonNull(resource.getContent()).getCoinEntity().getUuid())).withSelfRel());
        resource.add(linkTo(methodOn(CoinController.class)
                .getAllCoins()).withRel("all coins"));
        resource.add(linkTo(methodOn(CoinTypeController.class)
                .getByName(resource.getContent().getCoinEntity().getCoinTypeEntity().getName()))
                .withRel("coin type info"));
        resource.add(linkTo(methodOn(WalletController.class)
                .getByUUID(resource.getContent().getCoinEntity().getStoredInWalletEntity().getId()))
                .withRel("stored in wallet"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<CoinDto>> resources) {
        resources.add(linkTo(methodOn(CoinController.class)
                .getAllCoins()).withRel("all coins"));
    }
}

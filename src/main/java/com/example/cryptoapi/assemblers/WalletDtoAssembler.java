package com.example.cryptoapi.assemblers;

import com.example.cryptoapi.controllers.WalletController;
import com.example.cryptoapi.dtos.WalletDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WalletDtoAssembler implements SimpleRepresentationModelAssembler<WalletDto> {

    @Override
    public void addLinks(EntityModel<WalletDto> resource) {
        resource.add(linkTo(methodOn(WalletController.class)
                .getByUUID(Objects.requireNonNull(resource.getContent()).getWalletEntity().getId()))
                .withSelfRel());
        resource.add(linkTo(methodOn(WalletController.class)
                .getAllWallets()).withRel("all wallets"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<WalletDto>> resources) {
        resources.add(linkTo(methodOn(WalletController.class).getAllWallets()).withSelfRel());
    }
}

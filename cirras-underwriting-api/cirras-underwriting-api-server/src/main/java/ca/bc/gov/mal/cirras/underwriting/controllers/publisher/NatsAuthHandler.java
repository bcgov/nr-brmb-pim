package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nats.client.AuthHandler;
import io.nats.client.NKey;

public class NatsAuthHandler implements AuthHandler {

	private static final Logger logger = LoggerFactory.getLogger(NatsAuthHandler.class);

	private NKey nkey;

	public NatsAuthHandler(String seed) {
		nkey = NKey.fromSeed(seed.toCharArray());
	}

    @Override
	public byte[] sign(byte[] nonce) {
        try {
            return this.nkey.sign(nonce);
        } catch (GeneralSecurityException|IOException|NullPointerException ex) {
        	logger.error("Error occurred while signing nonce");
            return null;
        }
	}

	@Override
	public char[] getJWT() {
		return null;
	}

	@Override
	public char[] getID() {
        try {
            return this.nkey.getPublicKey();
        } catch (GeneralSecurityException|IOException|NullPointerException ex) {
        	logger.error("Error occurred while generating Public Key");
            return null;
        }
	}
}

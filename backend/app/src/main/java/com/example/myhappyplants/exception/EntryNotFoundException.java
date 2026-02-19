package com.example.myhappyplants.exception;

public class EntryNotFoundException extends Exception {
	private String entityName;

	public EntryNotFoundException(String message, String entityName) {
		super("Entry not found in database");
		this.entityName = entityName;
	}


}

package com.game.stacker.network;

import java.io.Serializable;

public interface Message extends Serializable {
	
	String getClientIdentifier();

}

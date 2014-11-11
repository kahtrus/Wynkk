
package com.soma.contraoller;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.soma.model.SignupModel;
import com.soma.model.SignupModel.Data;






public class SignupController
{

	/** The json. */
	private String json = "";

	/** The object mapper. */
	private ObjectMapper objectMapper = null;

	/** The json factory. */
	private JsonFactory jsonFactory = null;

	/** The jp. */
	private JsonParser jp = null;



	/** The _m Tests. */
	private SignupModel _mTests = null;

	/**
	 * Instantiates a new SignupController controller.
	 */
	public SignupController()
	{
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
	}

	/**
	 * Inits the.
	 *
	 * @param jsonstr the jsonstr
	 */
	public void init(String jsonstr)
	{
		json = jsonstr;

		try
		{
			jp = jsonFactory.createJsonParser(json);
			_mTests = objectMapper.readValue(jp, SignupModel.class);

		}

		catch (JsonParseException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}



	public String findStatus()
	{
		return _mTests.get_status();
	}

	public String findMessage()
	{
		return _mTests.get_message();

	}
	public ArrayList<Data> findAll()
	{
		return _mTests.getsUserData();
	}



}

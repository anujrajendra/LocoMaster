package com.cris.loco_master.webservice.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.onwbp.com.google.gson.JsonElement;
import com.onwbp.com.google.gson.JsonParser;

@SuppressWarnings("rawtypes")
@Provider
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class GsonJaxrsProvider implements MessageBodyReader<JsonElement>, MessageBodyWriter<JsonElement> {
	public static final String ENCODING = "UTF-8";

	public long getSize(JsonElement jsonElement, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
		return 0L;
	}

	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return true;
	}

	public void writeTo(JsonElement jsonElement, Class arg1, Type arg2, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, OutputStream responseStream) throws IOException, WebApplicationException {
		BufferedWriter streamWriter = new BufferedWriter(new OutputStreamWriter(responseStream, "UTF-8"));
		streamWriter.write(jsonElement.toString());
		streamWriter.flush();
	}

	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return true;
	}

	@SuppressWarnings("deprecation")
	public JsonElement readFrom(Class object, Type arg1, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap httpHeaders, InputStream inputStream) throws IOException, WebApplicationException {
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		JsonParser parser = new JsonParser();
		return parser.parse(streamReader);
	}
}
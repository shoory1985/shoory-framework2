package com.shoory.framework.starter.api.request;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.DataFrameReader;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDataFrameRequest extends BaseRequest {

	public abstract void fromDataFrame(byte[] dataFrame);
	
	protected void fromDataFrame(byte[] dataFrame, DataFrameReader reader) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new ByteArrayInputStream(dataFrame));
			reader.read(dis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dis = null;
			}
		}
	}

}

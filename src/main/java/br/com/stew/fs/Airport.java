package br.com.stew.fs;

public enum Airport
{

	private String desc;

	Airport(String desc)
	{
		this.desc = desc;
	}
	
	public String getDesc()
	{
		return desc;
	}
}

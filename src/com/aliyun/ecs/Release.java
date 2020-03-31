package com.aliyun.ecs;

public class Release
{
	int sec[] = new int[86400];
	String index[] = new String[86400];
	String timing[] = new String[86400];
	
	public void Add( int i, String date, String vmId )
	{
		sec[i] = 1;
		timing[i] = date;
		index[i] = vmId;
	}
	
	public void Remove( int i )
	{
		sec[i] = 0;
		timing[i] = null;
		index[i] = null;
	}
}

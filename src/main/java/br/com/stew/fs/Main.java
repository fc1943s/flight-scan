package br.com.stew.fs;

public class Main
{
	public static final String	APP_NAME	= "Flight Scan 1.0";

	public static void main(String[] args)
	{
		if(!Util.lockInstance(APP_NAME))
		{
			System.exit(1);
		}

		MainWindow mainWindow = new MainWindow();
		mainWindow.open();
	}
}

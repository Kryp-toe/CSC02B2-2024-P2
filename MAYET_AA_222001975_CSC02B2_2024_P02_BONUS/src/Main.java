import csc2b.server.WeatherServer;

public class Main
{
	public static void main(String[] args)
	{
		WeatherServer server = new WeatherServer(8888);
		Thread thread = new Thread(server);
		thread.run();
	}
}
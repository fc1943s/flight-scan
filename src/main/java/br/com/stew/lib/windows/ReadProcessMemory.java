package br.com.stew.lib.windows;
import br.com.stew.lib.windows.WindowsApi.Kernel32;
import br.com.stew.lib.windows.WindowsApi.User32;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class ReadProcessMemory
{
	private static final int	DEFAULT_BUFFER_SIZE	= 2000;

	private final Pointer		process;
	private Memory				outputBuffer;

	public ReadProcessMemory(String windowTitle)
	{
		int windowHandle = User32.INSTANCE.FindWindowA(null, windowTitle);

		if(windowHandle <= 0)
		{
			throw new RuntimeException("No window found with title '" + windowTitle + "'.");
		}

		PointerByReference pid = new PointerByReference();
		User32.INSTANCE.GetWindowThreadProcessId(windowHandle, pid);

		process = Kernel32.INSTANCE.OpenProcess(WindowsApi.PROCESS_QUERY_INFORMATION | WindowsApi.PROCESS_VM_READ, false, pid.getValue());

		if(process == null)
		{
			throw new RuntimeException("No PID found for window found with title '" + windowTitle + "'.");
		}
	}

	public byte[] read(int offset, int size)
	{
		outputBuffer = new Memory(size);
		Kernel32.INSTANCE.ReadProcessMemory(process, offset, outputBuffer, size, null);
		return outputBuffer.getByteArray(0, size);
	}

	public int search(String text, int startingOffset, int maxChars, int pos)
	{
		outputBuffer = new Memory(DEFAULT_BUFFER_SIZE);

		int result = -1;
		int foundCount = 0;

		TimeCount timeCount = new TimeCount();

		for(int i = 0; i < maxChars; i++)
		{
			byte[] bufferBytes = read(startingOffset + i, (int)outputBuffer.size());

			String output = new String(bufferBytes);

			// System.out.println(output);

			int index = output.indexOf(text);

			if(index == -1)
			{
				i += outputBuffer.size() - text.length();
			}
			else
			{
				foundCount++;
				if(foundCount < pos)
				{
					i += index + text.length() - 1;
				}
				else
				{
					result = startingOffset + i + index;
					break;
				}

			}
		}

		timeCount.logAndRestart();
		return result;
	}
}

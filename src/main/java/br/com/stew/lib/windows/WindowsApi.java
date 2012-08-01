package br.com.stew.lib.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public class WindowsApi
{
	public static final int	PROCESS_QUERY_INFORMATION	= 0x0400;
	public static final int	PROCESS_VM_READ				= 0x0010;

	public interface Kernel32 extends StdCallLibrary
	{
		Kernel32	INSTANCE	= (Kernel32)Native.loadLibrary("kernel32", Kernel32.class);

		public Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer dwProcessId);

		boolean ReadProcessMemory(Pointer hProcess, int inBaseAddress, Pointer outputBuffer, int nSize, IntByReference outNumberOfBytesRead);
	}

	public interface User32 extends StdCallLibrary
	{
		User32	INSTANCE	= (User32)Native.loadLibrary("user32", User32.class);

		public int GetWindowThreadProcessId(int hWnd, PointerByReference pid);

		public int FindWindowA(String clsName, String window);
	}
}

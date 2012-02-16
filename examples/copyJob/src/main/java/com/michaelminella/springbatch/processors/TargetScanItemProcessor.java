package com.michaelminella.springbatch.processors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import org.springframework.batch.item.ItemProcessor;

import com.michaelminella.springbatch.domain.Target;

public class TargetScanItemProcessor implements ItemProcessor<Target, Target> {

	public Target process(Target curTarget) throws Exception {
		try {
			System.out.println("About to check: " + curTarget.getIp() + ":" + curTarget.getPort());
			Socket socket = new Socket(curTarget.getIp(), curTarget.getPort());
			socket.setSoTimeout(2000);
			curTarget.setConnected(true);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			StringBuilder bannerBuffer = new StringBuilder();
			String banner;

			while ((banner = input.readLine()) != null) {
				bannerBuffer.append(banner);
			}
			
			curTarget.setBanner(bannerBuffer.toString());

			input.close();
			socket.close();
		} catch (Throwable ignore) {
		}

		return curTarget;
	}
}

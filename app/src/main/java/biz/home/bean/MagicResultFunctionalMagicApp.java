package biz.home.bean;

import java.io.Serializable;



public class MagicResultFunctionalMagicApp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1687573543528269473L;

	/*
	 * Magic程序动作
	 */
	private MagicResultFunctionalMagicAppOpenEnum magicAppOpen;

	private String content;

	public MagicResultFunctionalMagicApp() {

	}

	public MagicResultFunctionalMagicApp(
			MagicResultFunctionalMagicAppOpenEnum magicAppOpen) {
		this.magicAppOpen = magicAppOpen;
	}

	public MagicResultFunctionalMagicApp(
			MagicResultFunctionalMagicAppOpenEnum magicAppOpen, String content) {
		this.magicAppOpen = magicAppOpen;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MagicResultFunctionalMagicAppOpenEnum getMagicApp() {
		return magicAppOpen;
	}

	public void setMagicApp(MagicResultFunctionalMagicAppOpenEnum magicAppOpen) {
		this.magicAppOpen = magicAppOpen;
	}

}

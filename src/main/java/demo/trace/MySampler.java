package demo.trace;

import brave.sampler.Sampler;

public class MySampler extends Sampler {

	@Override
	public boolean isSampled(long arg0) {
		// TODO Auto-generated method stub
		System.out.println("isSampled Called with :"+arg0);
		return false;
	}

}

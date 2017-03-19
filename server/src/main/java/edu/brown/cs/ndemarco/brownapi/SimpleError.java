package edu.brown.cs.ndemarco.brownapi;

public class SimpleError implements Error {

	private String reason;
	private Exception exception;
	
	private SimpleError(Builder b) {
		this.reason = b.reason;
		this.exception = b.exception;
	}
	
	@Override
	public String userFriendlyReason() {
		return reason;
	}

	@Override
	public Exception exception() {
		return exception;
	}
	
	public static class Builder {
	
		private String reason;
		private Exception exception;
		
		public Builder withUserFriendlyReason(String reason) {
			this.reason = reason;
			return this;
		}
		
		public Builder withException(Exception e) {
			this.exception = e;
			return this;
		}
		
		public Error build() {
			return new SimpleError(this);
		}
		
	}

}

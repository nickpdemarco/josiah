package edu.brown.cs.ndemarco.josiah;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Josiah {
	
	private Set<QueryProcessor> processors = new HashSet<>();
	
	private Josiah(Builder b) {
		this.processors = b.defaultProcessors;
	}
	
	public JosiahFulfillment process(JosiahQuery query) {
		String intent = query.getResult().getMetadata().getIntentName();
		
		for (QueryProcessor p : processors) {
			if (p.isResponsibleFor(intent)) {
				return p.process(query);
			}
		}
		
		return JosiahFulfillment.error("That's something I don't know how to do.");
	}
	
	public static class Builder {
		private Set<QueryProcessor> defaultProcessors;
		
		public Builder() {
			this.defaultProcessors = new HashSet<>();
		}
		
		public Builder withDefaultProcessor(QueryProcessor processor) {
			defaultProcessors.add(processor);
			return this;
		}
		
		public Builder withDefaultProcessors(QueryProcessor ...processors) {
			defaultProcessors.addAll(Arrays.asList(processors));
			return this;
		}
		
		public Builder withDefaultProcessors(Collection<QueryProcessor> processors) {
			defaultProcessors.addAll(processors);
			return this;
		}
		
		public Josiah build() {
			return new Josiah(this);
		}
	}

}

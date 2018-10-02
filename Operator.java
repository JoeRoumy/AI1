public class Operator {

	//all possible actions
	public enum Action {Stab, Forward, RotLeft, RotRight};
	
	
	public Action action;
	
	public Operator(Action anAction) {

		action = anAction;
		
	}

}

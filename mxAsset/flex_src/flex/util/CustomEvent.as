package flex.util
{
	import flash.events.Event;
	
	import mx.containers.Panel;

	public class CustomEvent extends Event
	{
		public var panel:Panel = new Panel();
		
		public function CustomEvent(type:String,panel:Panel=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.panel = panel;
		}
		
	}
}
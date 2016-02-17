package cn.cnsasfram.helper{
	import com.benstucki.utilities.IconUtility;
	
	import mx.containers.Panel;
	import mx.containers.TitleWindow;
	import mx.controls.HScrollBar;
	import mx.controls.Tree;
	import mx.controls.VScrollBar;
	import mx.controls.scrollClasses.ScrollBar;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.IStyleManager2;

	/**
	 *Flex中common辅助类
	 *@author RLiuyong
	 */
	public class ASCSSClassHelper{		
		
		/**
		 * 使用IStyleManager对象
		 * */
		protected static var styleManager:IStyleManager2=FlexGlobals.topLevelApplication.styleManager;
		
		public function ASCSSClassHelper(){}
		
		/**
		 * 得到buttonCSS对象
		 * */
		public static function getButtonCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.Button");
		}
		
		/**
		 * 得到linkbuttonCSS对象
		 * */
		public static function getLinkButtonCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.LinkButton");
		}
		
		/**
		 * 得到ComBoxCSS对象
		 * */
		public static function getComBoxCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.ComboBox");
		}
		
		/**
		 * 得到TextInputCSS对象
		 * */
		public static function getTextInputCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.TextInput");
		}
		
		/**
		 *得到 NumericStepperCSS对象
		 * */
		public static function getNumericStepperCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.NumericStepper");
		}
		
		/**
		 * 得到DateFieldCSS对象
		 * */
		public static function getDateFieldCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.DateField");
		}
		
		/**
		 * 得到TooTipCSS对象
		 * */
		public static function getToolTipCSSClass():CSSStyleDeclaration{
			
			return styleManager.getStyleDeclaration("mx.controls.ToolTip");
		}
		
		/**
		 * 得到DataGridCSS对象
		 * */
		public static function getDataGridCSSClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.DataGrid");
		}
		
		/**
		 * 得到treeCss对象
		 * */
		public static function getTreeCssClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.Tree");
		}
		
		/**
		 * 得到VscrollbarCss对象
		 * */
		public static function getScrollbarCssClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.controls.scrollClasses.ScrollBar");
		}
		
		/**
		 * 得到PanelCss对象
		 * */
		public static function getPanelCssClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.containers.Panel");
		}
		
		/**
		 * 得到TitleWindowCSS对象
		 * */
		public static function getTitleWindowCssClass():CSSStyleDeclaration{
			return styleManager.getStyleDeclaration("mx.containers.TitleWindow");
		}
	}
}
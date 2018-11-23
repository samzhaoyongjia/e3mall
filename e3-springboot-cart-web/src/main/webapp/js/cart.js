var CART = {
	itemNumChange : function(){
		$(".increment").click(function(){//＋
			var _thisInput = $(this).siblings("input");
			_thisInput.val(eval(_thisInput.val()) + 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
				CART.refreshTotal_Price(_thisInput.attr("itemId"));
			});
		});
		$(".decrement").click(function(){//-
			var _thisInput = $(this).siblings("input");
			if(eval(_thisInput.val()) == 1){
				return ;
			}
			_thisInput.val(eval(_thisInput.val()) - 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
				CART.refreshTotal_Price(_thisInput.attr("itemId"));
			});
		});
		/*$(".itemnum").change(function(){
			var _thisInput = $(this);
			$.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
				CART.refreshTotalPrice();
			});
		});*/
	},
	refreshTotalPrice : function(){ //重新计算总价
		var total = 0;
		$(".itemnum").each(function(i,e){
			var _this = $(e);
			total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
		});
		$("#allMoney2").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
			 prefix: '¥',
			 thousandsSeparator: ',',
			 centsLimit: 2
		});
	},
	refreshTotal_Price : function(itemId){ //重新计算商品总价
		$(".itemnum").each(function(i,e){
			var total = 0;
			var _this = $(e);
			//判断选择的元素id是否是改变的元素id
			if (_this.attr("itemId") == itemId) {
				total = (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
				//选择遍历中的小计span标签改变其价格 父类的父类的后面兄弟的子节点spen
				(_this.parent().parent().nextAll().children("span")).html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
					 prefix: '¥',
					 thousandsSeparator: ',',
					 centsLimit: 2
				});
			}
		});
		
	}
};

$(function(){
	CART.itemNumChange();
});
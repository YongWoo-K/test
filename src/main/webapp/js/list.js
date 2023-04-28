/*
누락된 부분이나 첨부파일의 형식을 체크
 */
window.onload = function(){
	//도큐먼트에서 id가 submit1인 것을 클릭시
	document.getElementById('submit1').onclick = function(){
		//form태그 속 개인정보 동의 부분의 체크박스를 체크하지 않았을 경우
		if(document.wfrm.info.checked == false){
			//알림창 표시 + false 리턴
			alert('개인정보 수집 및 이용에 동의를 해주세요.');
			return false;
			//-> a태그가 click이벤트를 받았을 때 false값을 리턴시켜서 입력데이터를 보내는 동작을 중지시키는것
		}
		
		//작성자를 입력하지 않았을 경우
		if(document.wfrm.writer.value.trim() == ''){
			//알림창 표시 + false 리턴
			alert('작성자를 입력해주세요.');
			return false;
		}
		
		//제목을 입력하지 않았을 경우
		if(document.wfrm.subject.value.trim() == ''){
			//알림창 표시 + false 리턴
			alert('제목을 입력해주세요.');
			return false;
		}
		
		//비밀번호를 입력하지 않았을 경우
		if(document.wfrm.password.value.trim() == ''){
			//알림창 표시 + false 리턴
			alert('비밀번호를 입력해주세요.');
			return false;
		}
		
		//파일을 첨부하지 않았을 경우
		if(document.wfrm.upload.value.trim() == ''){
			//알림창 표시 + false 리턴
			alert('파일을 첨부해주세요.');
			return false;
		}
		
		//첨부파일 확장자 체크(사진파일만 첨부하도록)
		var fileValue = document.wfrm.upload.value.trim().split('\\');//첨부파일
		var fileName = fileValue[fileValue.length-1];//파일명
		var fileEname = fileName.subString(fileName.length-4,fileName.length);//첨부파일형식(확장자)
		if(fileEname == '.jpg' || fileEname == '.png' || fileEname == ".gif"){
			
		}
		//사진파일이 아닌 파일을 첨부했다면
		else{
			//알림창 + 첨부파일 제거 + false리턴
			alert('잘못된 파일입니다. 사진파일만 첨부해주세요.(jpg 또는 png, gif 등)')
			document.wfrm.upload.value = '';
			return false;
		}
		document.wfrm.submit();
	}
}
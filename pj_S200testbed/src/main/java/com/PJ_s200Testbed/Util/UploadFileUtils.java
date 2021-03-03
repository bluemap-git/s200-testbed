package com.PJ_s200Testbed.Util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadFileUtils {

	public static String calcPath(String uploadPath) {
		Calendar cal = Calendar.getInstance();
		// 오늘치 달력값을 가지고와라

		String yearPath = File.separator + cal.get(Calendar.YEAR);
		// separator 구분자 (\) + 오늘치 달력 값에서 year을 가지고 와라

		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);

		// new DecimalFormat("00") -> 예를들어 4월의 경우 04로 나오도록 변환
		// \2020 + \ + 04

		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		// \2020\04\09

		String path = uploadPath + datePath;

		File dirPath = new File(path);

		if (!dirPath.exists())
			dirPath.mkdirs();

		// makeDir(uploadPath, yearPath, monthPath, datePath);
		// 디렉토리를 만들어서 uploadPath, yearPath, monthPath, datePath 담는다
		// 같은 클래스 내에 있어서 객체 생성할 필요가 없다

		log.info(datePath);
		return path;
	}


}

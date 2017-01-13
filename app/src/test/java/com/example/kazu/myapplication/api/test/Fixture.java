package com.example.kazu.myapplication.api.test;

/**
 * Created by kbx on 2017/01/13.
 */

import org.robolectric.res.Fs;
import org.robolectric.res.FsFile;
import org.robolectric.util.Strings;

import java.io.IOException;

/**
 * ファイルからテスト用データを読み込むクラス
 */
public class Fixture {

    public static String load(String path) throws IOException {
        FsFile fixturePath = Fs.currentDirectory().join("src", "test", "fixtures");
        return Strings.fromStream(fixturePath.join(path).getInputStream());
    }

}

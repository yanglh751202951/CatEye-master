package com.cicinnus.cateye.module.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cicinnus.cateye.R;
import com.cicinnus.cateye.base.BaseFragment;
import com.cicinnus.cateye.tools.Person;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class MineFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.sign_in_and_up)
    LinearLayout signInAndUp;

    @BindView(R.id.sign_in_ed)
    LinearLayout signInEd;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.sign_in)
    Button sign_in;

    @BindView(R.id.sign_up)
    Button sign_up;

    @BindView(R.id.sign_out)
    Button sign_out;

    @BindView(R.id.sign_in_name)
    TextView sign_in_name;

    private SharedPreferences sp;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEventAndData() {
        Bmob.initialize(getActivity(), "518fdb8e074a4cda37c4a5a491776a60");
        sp = getActivity().getSharedPreferences("person", Context.MODE_PRIVATE);
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        sign_out.setOnClickListener(this);
        String name1 = sp.getString("name", null);
        if (name1 != null) {
            signInAndUp.setVisibility(View.GONE);
            signInEd.setVisibility(View.VISIBLE);
            sign_in_name.setText("用户：" + name1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                final String name1 = name.getText().toString();
                final String password1 = password.getText().toString();
                if (name1.equals("")) {
                    Toast.makeText(getActivity(),"用户名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    BmobQuery<Person> query = new BmobQuery<>();
                    query.addWhereEqualTo("name", name1).findObjects(new FindListener<Person>() {
                        @Override
                        public void done(List<Person> list, BmobException e) {
                            if (e == null) {
                                if (list.size() == 0) {
                                    Toast.makeText(getActivity(),"用户不存在", Toast.LENGTH_SHORT).show();
                                } else if (!(list.get(0)).getPassword().equals(password1)){
                                    Toast.makeText(getActivity(),"密码错误", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(),"登陆成功", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("name", name1);
                                    editor.putString("password", password1);
                                    editor.apply();
                                    signInAndUp.setVisibility(View.GONE);
                                    signInEd.setVisibility(View.VISIBLE);
                                    sign_in_name.setText("用户：" + name1);
                                }
                            } else {
                                Log.e("bmob", "获取用户信息失败");
                            }
                        }
                    });
                }
                break;
            case R.id.sign_up:
                final String name2 = name.getText().toString();
                final String password2 = password.getText().toString();
                if (name2.equals("") || password2.equals("")) {
                    Toast.makeText(getActivity(),"用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    BmobQuery<Person> query = new BmobQuery<>();
                    query.addWhereEqualTo("name", name2).findObjects(new FindListener<Person>() {
                        @Override
                        public void done(List<Person> list, BmobException e) {
                            if (e == null) {
                                if (list.size() != 0) {
                                    Toast.makeText(getActivity(), "用户名重复", Toast.LENGTH_SHORT).show();
                                } else {
                                    Person person = new Person(name2, password2);
                                    person.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putString("name", name2);
                                                editor.putString("password", password2);
                                                editor.apply();
                                                signInAndUp.setVisibility(View.GONE);
                                                signInEd.setVisibility(View.VISIBLE);
                                                sign_in_name.setText("用户：" + name2);
                                            } else {
                                                Toast.makeText(getActivity(), "注册失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.sign_out:
                signInAndUp.setVisibility(View.VISIBLE);
                signInEd.setVisibility(View.GONE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("name");
                editor.remove("password");
                editor.apply();
        }
    }
}

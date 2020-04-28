package nguyenthu.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public  class Controller implements Cloneable {

	/**
	 * アカウントビーンの名称
	 */
	public static final String ACCOUNT_BEAN_NAME = "_account";

	/**
	 * サーブレット
	 */
	private HttpServlet _servlet;

	/**
	 * HTTP リクエスト
	 */
	private HttpServletRequest _request;

	/**
	 * HTTP レスポンス
	 */
	private HttpServletResponse _response;

	/**
	 * Exception
	 */
	private Exception _exception;

	/**
	 * MultipartFormDataRequest
	 */
	/**
	 * アクセスログ
	 */
	protected PrintWriter _accesslog;

	/**
	 * 作業ディレクトリ
	 */
	protected String _tempdir;

	/**
	 * コネクションプール
	 */

	
	private HashMap<String, ArrayList<String>> _rolesMap;

	protected void setFields(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response) {

		if (servlet == null) {
			throw new NullPointerException("servlet は null ではなりません。");
		}
		if (request == null) {
			throw new NullPointerException("request は null ではなりません。");
		}
		if (response == null) {
			throw new NullPointerException("response は null ではなりません。");
		}

		_servlet = servlet;
		_request = request;
		_response = response;
	}

	
	
	protected void executeConfusedAccountAction() {
		try {
			getResponse().sendRedirect("certify");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * レスポンス出力後に発生したエラーのためのエラーアクションを実行する。
	 */
	protected void executeErrorActionAfterForward() {
		try {
			_exception.printStackTrace();
			getResponse().getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * アクションを実行する。
	 * 
	 * @param servlet  サーブレット
	 * @param request  HTTP リクエスト
	 * @param response HTTP レスポンス
	 */
	

	/**
	 * Viewを表示する
	 * 
	 * @param view 表示リソースパス
	 * @throws ServletException
	 * @throws IOException
	 */
	public void dispatchView(String view) throws ServletException, IOException {	
//		try {
			RequestDispatcher dispatcher = getRequest().getRequestDispatcher(view);
			dispatcher.forward(getRequest(), getResponse());
//		} catch (IllegalStateException e) {
//			System.out.println("request redirected");
//		}catch (Exception e) {
//			System.out.println("dispatchView: error");
//			e.printStackTrace();
//		}
	}

	/**
	 * パラメータを得る。
	 * 
	 * <pre>
	 *   1. マルチパートフォームデータに対応
	 *   2. パラメータ値を正しい日本語へ変換
	 * </pre>
	 * 
	 * @param name パラメータの名称
	 * @return 値
	 */
	public String getParameter(String name) {
		String value;
//		if (isMultipartFormData()) {
//			MultipartFormDataRequest multi = getMultipartFormDataRequest();
//			value = multi.getParameter(name);
//		} else {
//			value = getRequest().getParameter(name);
//		}
		value = getRequest().getParameter(name);
		return filterParameter(value);
	}

	public String[] getParameterValues(String name) throws UnsupportedEncodingException {
		String[] tmp;

		tmp = _request.getParameterValues(name);
		String[] value;
		if (null != tmp) {
			value = new String[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				value[i] = filterParameter(tmp[i]);
			}
		} else {
			value = new String[0];
		}

		return value;
	}

	/**
	 * パラメータを int 型として得る。
	 * 
	 * @param name パラメータの名称
	 * @return 値
	 * @throws NumberFormatException int 値として取得できなかった場合
	 */
	public int getIntParameter(String name) throws NumberFormatException {
		String s = getParameter(name);
		return Integer.parseInt(s);
	}

	/**
	 * パラメータを long 型として得る。
	 * 
	 * @param name パラメータの名称
	 * @return 値
	 * @throws NumberFormatException long 値として取得できなかった場合
	 */
	public long getLongParameter(String name) throws NumberFormatException {
		String s = getParameter(name);
		return Long.parseLong(s);
	}

	/**
	 * アプリケーションを得る。
	 * 
	 * @return アプリケーション(サーブレットコンテキスト)
	 */
	public ServletContext getApplication() {
		return _servlet.getServletConfig().getServletContext();
	}

	/**
	 * HTTP セッションを得る。
	 * 
	 * @return HTTP セッション
	 */
	public HttpSession getSession() {
		return _request.getSession();
	}

	/**
	 * HTTP リクエストを得る。
	 * 
	 * @return HTTP リクエスト
	 */
	public HttpServletRequest getRequest() {
		return _request;
	}

	/**
	 * HTTP レスポンスを得る。
	 * 
	 * @return HTTP レスポンス
	 */
	public HttpServletResponse getResponse() {
		return _response;
	}

	/**
	 * Exceptionを得る。
	 * 
	 * @return Exception
	 */
	public Exception getError() {
		return _exception;
	}

	/**
	 * Exceptionを設定する。
	 * 
	 * @param Exception
	 */
	public void setError(Exception e) {
		_exception = e;
	}

	/**
	 * アプリケーションスコープのビーンを得る。
	 * 
	 * @param name ビーンの名称
	 * @return ビーン
	 */
	public Object getApplicationBean(String name) {
		return getApplication().getAttribute(name);
	}

	/**
	 * アプリケーションスコープのビーンを得る。 ビーンが存在しない場合はビーンを生成してアプリケーションスコープのビーン として設定する。
	 * このメソッドは、ビーンが既に存在している場合、そのビーンの型が type と同 型かどうかに関心を持っていない。
	 * 
	 * @param name ビーンの名称
	 * @param type ビーンの型
	 * @return ビーン
	 */
	public Object getApplicationBean(String name, Class<?> type) {
		Object bean = getApplication().getAttribute(name);
		if (bean == null) {
			bean = newInstance(type);
			setApplicationBean(name, bean);
		}
		return bean;
	}

	/**
	 * アプリケーションスコープのビーンを設定する。
	 * 
	 * @param name ビーンの名称
	 * @param bean ビーン
	 */
	public void setApplicationBean(String name, Object bean) {
		getApplication().setAttribute(name, bean);
	}

	/**
	 * セッションスコープのビーンを得る。
	 * 
	 * @param name ビーンの名称
	 * @return ビーン
	 */
	public Object getSessionBean(String name) {
		return getSession().getAttribute(name);
	}

	/**
	 * セッションスコープのビーンを得る。 ビーンが存在しない場合はビーンを生成して セッションスコープのビーンとして設定する。
	 * このメソッドは、ビーンが既に存在している場合、 そのビーンの型が type と同型かどうかに関心を持っていない。
	 * 
	 * @param name ビーンの名称
	 * @param type ビーンの型
	 * @return ビーン
	 */
	public Object getSessionBean(String name, Class<?> type) {
		Object bean = getSession().getAttribute(name);
		if (bean == null) {
			bean = newInstance(type);
			setSessionBean(name, bean);
		}
		return bean;
	}

	/**
	 * セッションスコープのビーンを設定する。
	 * 
	 * @param name ビーンの名称
	 * @param bean ビーン
	 */
	public void setSessionBean(String name, Object bean) {
		getSession().setAttribute(name, bean);
	}

	/**
	 * リクエストスコープのビーンを得る。
	 * 
	 * @param name ビーンの名称
	 * @return ビーン
	 */
	public Object getRequestBean(String name) {
		return getRequest().getAttribute(name);
	}

	/**
	 * リクエストスコープのビーンを得る。 ビーンが存在しない場合はビーンを生成して リクエストスコープのビーンとして設定する。
	 * このメソッドは、ビーンが既に存在している場合、 そのビーンの型が type と同型かどうかに関心を持っていない。
	 * 
	 * @param name ビーンの名称
	 * @param type ビーンの型
	 * @return ビーン
	 */
	public Object getRequestBean(String name, Class<?> type) {
		Object bean = getRequest().getAttribute(name);
		if (bean == null) {
			bean = newInstance(type);
			setRequestBean(name, bean);
		}
		return bean;
	}

	/**
	 * リクエストスコープのビーンを設定する。
	 * 
	 * @param name ビーンの名称
	 * @param bean ビーン
	 */
	public void setRequestBean(String name, Object bean) {
		getRequest().setAttribute(name, bean);
	}

	/**
	 * アクションリストを得る。
	 * 
	 * @return アクションリスト
	 * @throws IOExceprion
	 */
	
	
	protected static Object newInstance(Class<?> type) {
		try {
			return type.newInstance();
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage());
		} catch (InstantiationException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * コンテントタイプの検査。 マルチパートフォームデータであればその準備をする。
	 * 
	 * @throws IOException IO アクセスエラーが発生した場合
	 */
	protected void checkContentType() throws IOException {
		// multipart/form-dataの場合はMultipartFormDataRequestを生成
//		if (isMultipartFormData()) {
//			String dirName = createTempDir().getAbsolutePath();
//			HttpServletRequest request = getRequest();
//			_multi = new MultipartFormDataRequest(request, dirName);
//		}
	}

	/**
	 * ログ出力
	 */

	/**
	 * 権限の検査
	 * 
	 * @throws SessionErrorException
	 * @throws ConfusedAccountException 
	 */


	/**
	 * HTTPリクエストのパラメータを正しい文字列に変換する。 このメソッドはHTTPクライアントがパラメータの文字列を
	 *
	 * @param s HTTPリクエストのパラメータ
	 * @return 変換された文字列
	 */
	protected String filterParameter(String s) {
		try {
			if (s != null) {
//				String encoding = getRequest().getCharacterEncoding();
				return new String(s.getBytes("UTF-8"), "UTF-8");
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getClass().getName() + ':' + e.getMessage());
		}
	}

	/**
	 * リクエストのコンテントタイプが multipart/form-data か
	 * 
	 * @param req HTTPリクエスト
	 * @return マルチパートフォームデータならtreu。そうでなければfalse。
	 */
	protected boolean isMultipartFormData() {
		String type = getRequest().getContentType();
		return type != null && type.toLowerCase().startsWith("multipart/form-data");
	}

	/**
	 * HTTP リクエストのパラメータ名を列挙する。
	 * 
	 * @return ファイル名の列挙
	 */
	
	/**
	 * マルチパートフォームデータ形式のリクエストのファイル名を列挙する。 このメソッドはリクエストのContentTypeが
	 * multipart/form-dataでない場合はnullを返す。
	 * 
	 * @return ファイルのパラメータ名の列挙
	 */
	

	/**
	 * マルチパートフォームデータ形式のリクエストのファイルを取得する。 このメソッドはリクエストのContentTypeが
	 * multipart/form-dataでない場合はnullを返す。
	 * 
	 * @param name ファイルのパラメータ名
	 * @return ファイル
	 */
	

	/**
	 * マルチパートフォームデータ形式のリクエストのファイルのファイルシステム上 の名称を取得する。このメソッドはリクエストのContentTypeが
	 * multipart/form-dataでない場合はnullを返す。
	 * 
	 * @param name ファイルのパラメータ名
	 * @return ファイルシステム上の名称
	 */
	

	/**
	 * HTTP セッション毎の作業ディレクトリの名称を得る。
	 * 
	 * @return ディレクトリ名
	 */
	public String getTempDirName() {
		if (_tempdir == null) {
			throw new RuntimeException("作業用ディレクトリが指定されていません");
		}

		HttpSession session = getSession();
		long time = session.getCreationTime();
		String sessionId = session.getId();
		String user = "unknown";
		String ip = getRequest().getRemoteAddr();
		StringBuffer sb = new StringBuffer(256);
		sb.append(_tempdir);
		sb.append('/');
		sb.append(time);
		sb.append('-');
		sb.append(sessionId);
		sb.append('-');
		sb.append(user);
		sb.append('-');
		sb.append(ip);
		return sb.toString();
	}

	/**
	 * HTTP セッション毎の作業ディレクトリを生成する。 既にディレクトリが存在する場合はそのディレクトリを返す。
	 * 
	 * @return ディレクトリ
	 */
	public File createTempDir() {
		String dirName = getTempDirName();
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * JDBC コネクションを取得する。 このメソッドは JDBCコネクションをコネクションプールから取得する。 取得されたコネクションはロックされるため、
	 * 利用者はコネクションの使用後は 必ず freeConnection() を使用して解放しなければならない。
	 * 
	 * @return JDBC コネクション
	 * @throws SQLException データベースアクセスエラーが発生した場合
	 */


	/**
	 * 取得したコネクションを解放する。
	 * 
	 * @param con JDBC コネクション
	 * @throws SQLException データベースアクセスエラーが発生した場合
	 */


	/**
	 * アクセスログを設定する
	 * 
	 * @param log ログファイル
	 */
	public void setAccessLog(PrintWriter pw) {
		_accesslog = pw;
	}

	/**
	 * 作業ディレクトリを設定する
	 * 
	 * @param 作業ディレクトリ
	 */
	public void setTempDir(String dir) {
		_tempdir = dir;
	}

	/**
	 * XXXX コネクションプールを設定する
	 * 
	 * @param pool
	 */


	/**
	 * @return the _rolesMap
	 */
	
	
	
}

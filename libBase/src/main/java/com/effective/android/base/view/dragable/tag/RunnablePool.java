package com.effective.android.base.view.dragable.tag;

import android.app.Activity;
import android.os.Build;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * the runnable pool help get a runnable from the pool.
 *  the IRunnbleExecutor maybe Activity or Fragment . but i have cared about them.
 *  <p>and the 'runner' from cache will automatic be recycled after run. </p>
 * Created by heaven7 on 2016/6/7.
 */
public final class RunnablePool {

    private final Cacher<Runner,Void> mCacher;
    private static RunnablePool sPool;

    public RunnablePool(int maxPoolSize) {
        this.mCacher = new Cacher<Runner,Void>(maxPoolSize) {
            @Override
            public Runner create(Void aa) {
                return new Runner() {
                    @Override
                    public void run() {
                        super.run();
                        recycle(this);
                    }
                };
            }
        };
    }

    /**
     * obtain a Runner from the  pool. and the {@link RunnablePool.Runner}
     *  will automatic be recycled after run.
     * @param executor the really runnable execute
     * @param what what message to execute
     * @param params the params to execute
     * @return the Runner from cache.
     */
    public Runner obtain(IRunnbleExecutor executor, int what,Object...params){
        final Runner runner = mCacher.obtain();
        runner.setExecutor(executor);
        runner.setWhat(what);
        runner.setParams(params);
        return runner;
    }

    public static Runner obtainRunner(IRunnbleExecutor executor, int what,Object...params){
        return getDefault().obtain(executor,what,params);
    }

    public static synchronized RunnablePool getDefault(){
        if(sPool == null){
            sPool = new RunnablePool(10);
        }
        return sPool;
    }

    /**
     * this is the runnable class help we reuse the runnable object.so it's high efficiency .
     * and after the {@link Runner#run()} is called. the Runner will atonmic be recycled to the cacher.
     */
    public static class Runner implements Runnable{

        private Object[] mParams;
        private IRunnbleExecutor mExecutor;
        private int what;
        private WeakReference<IRunnbleExecutor> mWeakExecutor;

        public void setParams(Object[] mParams) {
            this.mParams = mParams;
        }
        public void setExecutor(IRunnbleExecutor mExecutor) {
            if(mExecutor instanceof Fragment || mExecutor instanceof android.app.Fragment
                    || mExecutor instanceof Activity){
                this.mWeakExecutor = new WeakReference<>(mExecutor);
            }else {
                this.mExecutor = mExecutor;
            }
        }
        public int getWhat() {
            return what;
        }
        public void setWhat(int what) {
            this.what = what;
        }

        public Object[] getParams() {
            return mParams;
        }
        public IRunnbleExecutor getExecutor() {
            return mWeakExecutor!=null ? mWeakExecutor.get(): mExecutor;
        }

        @Override
        public void run() {
            final IRunnbleExecutor executor = getExecutor();
            if(executor == null){
                System.err.println("RunnablePool_Runner_run : executor == null or is recycled(Fragment or Activity)");
                return;
            }
            boolean shouldExecute = true;
            if(executor instanceof Activity){
                if(((Activity) executor).isFinishing()){
                    System.out.println("RunnablePool_Runner_run : executor is Activity and isFinishing() = true. ");
                    shouldExecute = false;
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                        && ((Activity) executor).isDestroyed()){
                    System.out.println("RunnablePool_Runner_run : executor is Activity and isDestroyed() = true. ");
                    shouldExecute = false;
                }
            }else if(executor instanceof Fragment){
                if(((Fragment) executor).isDetached() || ((Fragment) executor).isRemoving()){
                    System.out.println("RunnablePool_Runner_run : executor is Fragment and isDestroyed()" +
                            " ||  isRemoving() = true. ");
                    shouldExecute = false;
                }
            }else if(executor instanceof android.app.Fragment){
                if( (Build.VERSION.SDK_INT >=13 && ((android.app.Fragment) executor).isDetached())
                        || ((android.app.Fragment) executor).isRemoving()){
                    System.out.println("RunnablePool_Runner_run : executor is android.app.Fragment" +
                            " and isDestroyed() ||  isRemoving() = true. ");
                    shouldExecute = false;
                }
            }
            if(shouldExecute) {
                executor.execute(getWhat(), getParams());
            }
            reset();
        }

        /** reset the all member of this */
        protected void reset() {
            this.mWeakExecutor = null;
            this.mExecutor = null;
            this.mParams = null;
        }
    }

    /**
     * this is the runnable executor
     */
    public interface IRunnbleExecutor{

        /**
         *  execute the command impl
         *  @param what indicate which is the executor
         *  @param params  the params to execute
         */
        void execute(int what, Object... params);
    }
}
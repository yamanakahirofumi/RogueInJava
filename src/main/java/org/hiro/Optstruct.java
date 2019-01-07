package org.hiro;

import java.lang.reflect.Method;

public class Optstruct {
    String o_name;	/* option name */
    String	o_prompt;	/* prompt for interactive entry */
    Object o_opt;		/* pointer to thing to set */
//    /* function to print value */
    Method o_putfunc;
//    /* function to get value interactively */
    Method o_getfunc;
//    int		(*o_getfunc)(void *opt, WINDOW *win);

    public Optstruct(String o_name,String o_prompt, Object o_opt){
        this.o_name = o_name;
        this.o_prompt = o_prompt;
        this.o_opt = o_opt;
    }


}

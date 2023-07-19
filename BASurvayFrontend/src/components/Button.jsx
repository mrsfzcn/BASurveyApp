function classNames(...classNamesList) {
    return classNamesList.filter(Boolean).join(" ");
  }
  
  function Button({ children, primary, secondary, rounded,circle, bold,fat, ...rest }) {
    const classes = classNames(
      rest.className,
      "px-5 py-1  md:w-30 xl:w-40",
      primary && "bg-[#64E9B1] text-black ",
      secondary && "bg-[#EB5353] text-black",
      bold && "font-bold",
      rounded && "rounded-lg",
      fat && "px-12 py-4",
      circle && "rounded-full"
      //Ek özellik istendiği zaman buraya , ile ayırıp ekleme yapabiliyoruz
      //Tailwindcss
    );
    return (
      <button {...rest} className={classes}>
        {children}
      </button>
    );
  }
  
  Button.propTypes = {
    checkVariationValue: ({ primary, secondary }) => {
      const count = Number(!!primary) + Number(!!secondary);
  
      if (count > 1) {
        return new Error("Only one of primary, secondary can be true");
      }
    },
  };
  
  export default Button;
  
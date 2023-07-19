import classNames from "classnames";

function Input({
  value,
  type,
  placeholder,
  fit,
  full,
  half,
  eigthypercent,
  disabled,
  required,
  ...rest
}) {
  const classes = classNames(
    rest.className,
    "p-2 text-lg bg-transparent border-b border-black outline-none text-black transition disabled:text-white disabled:bg-neutral-900 disabled:opacity-40 disabled:cursor-not-allowed",
    {
      "w-fit": fit,
      "w-full": full,
      "w-1/2": half,
      "w-4/5": eigthypercent,
    }
  );

  return (
    <input
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={rest.onChange}
      className={classes}
      disabled={disabled}
      required={required}
    />
  );
}

export default Input;

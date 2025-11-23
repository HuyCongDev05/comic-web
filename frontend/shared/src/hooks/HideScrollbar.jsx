import { useEffect } from "react";

export default function useHideScrollbar() {
  useEffect(() => {
    const style = document.createElement("style");
    style.id = "dynamicHideScrollbar";
    style.innerHTML = `
      html,
      body,
      #root {
        overflow-y: scroll;
        -ms-overflow-style: none;
        scrollbar-width: none;
      }

      html::-webkit-scrollbar,
      body::-webkit-scrollbar {
        width: 0px;
        background: transparent;
      }
    `;
    document.head.appendChild(style);

    return () => {
      const el = document.getElementById("dynamicHideScrollbar");
      if (el) el.remove();
    };
  }, []);
}

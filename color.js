const colorMatrix = 


document.addEventListener("DOMContentLoaded", () => {
  console.log("color.js loaded");
  console.log(colorMatrix);
  colorMatrix.forEach((colorLine) => {
    const tr = document.createElement("tr");
    colorLine.forEach((color) => {
      const td = document.createElement("td");
      td.style.width = "2px";
      td.style.height = "2px";
      const r = (color & 0xff0000) >> 16;
      const g = (color & 0xff00) >> 8;
      const b = color & 0xff ;
      td.style.backgroundColor = "rgb(" + r + "," + g +","+b+ ")";
      tr.appendChild(td);
    })
    document.getElementById("table").appendChild(tr);

  });
});